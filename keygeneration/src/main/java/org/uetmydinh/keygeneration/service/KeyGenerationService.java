package org.uetmydinh.keygeneration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.uetmydinh.keygeneration.config.KeyCacheConfig;
import org.uetmydinh.keygeneration.entity.Key;
import org.uetmydinh.keygeneration.tasks.KeyCacheRefillTask;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class KeyGenerationService {
    private final MongoTemplate mongoTemplate;
    private final Queue<Key> keyCache;
    private final KeyCacheConfig keyCacheConfig;
    private final ScheduledExecutorService scheduler;
    private final KeyCacheRefillTask keyCacheRefillTask;
    private final AtomicBoolean refillInProgress = new AtomicBoolean(false);
    private int batchSize;
    private static final Logger log = LoggerFactory.getLogger(KeyGenerationService.class);

    @Autowired
    public KeyGenerationService(MongoTemplate mongoTemplate, KeyCacheConfig keyCacheConfig) {
        this.mongoTemplate = mongoTemplate;
        this.keyCacheConfig = keyCacheConfig;
        this.keyCache = keyCacheConfig.keyCache();
        this.batchSize = keyCacheConfig.initialBatchSize();
        this.keyCacheRefillTask = new KeyCacheRefillTask(mongoTemplate, keyCache, batchSize);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        refillCache();
        scheduleRefill();
    }

    public Optional<Key> getAvailableKey() {
        Key key = keyCache.poll();
        if (key == null) {
            log.warn("[CACHE MISS] Fetching key directly from database");
            key = getAvailableKeyFromDatabase();
            if (key != null) {
                log.debug("Fetched key directly from database: {}", key.getId());
                asyncRefillCacheIfNeeded();
            } else {
                log.error("No more keys available in database.");
            }
        } else {
            log.debug("[CACHE HIT] Fetched key from cache: {}", key.getId());
        }
        return Optional.ofNullable(key);
    }

    private Key getAvailableKeyFromDatabase() {
        Query query = new Query(Criteria.where("isUsed").is(false));
        Update update = new Update().set("isUsed", true);
        return mongoTemplate.findAndModify(query, update, Key.class);
    }

    private void asyncRefillCacheIfNeeded() {
        if (keyCache.size() < batchSize / 2 && refillInProgress.compareAndSet(false, true)) {
            CompletableFuture.runAsync(this::refillCache)
                    .whenComplete((result, error) -> {
                        refillInProgress.set(false);
                        if (error != null) {
                            log.error("Refill cache encountered an error", error);
                        }
                    });
        } else {
            log.debug("Cache refill not needed");
        }
    }

    private void refillCache() {
        try {
            int currentCacheSize = keyCache.size();
            log.info("Refilling cache with {} keys", batchSize);
            keyCacheRefillTask.run();
            int refilledCount = keyCache.size() - currentCacheSize;
            adjustBatchSize(refilledCount);
        } catch (Exception e) {
            log.error("Error during cache refill", e);
        }
    }

    private void adjustBatchSize(int refilledCount) {
        int oldBatchSize = this.batchSize;
        if (refilledCount < batchSize / 2) {
            batchSize = Math.max(keyCacheConfig.minBatchSize(), batchSize / 2);
        } else if (refilledCount >= batchSize) {
            batchSize = Math.min(keyCacheConfig.maxBatchSize(), batchSize * 2);
        }
        log.debug("Adjusted batch size from {} to: {}", oldBatchSize, batchSize);
    }

    private void scheduleRefill() {
        scheduler.scheduleAtFixedRate(() -> {
            log.debug("Start scheduled cache refill");
            asyncRefillCacheIfNeeded();
        }, 0, keyCacheConfig.refillInterval(), TimeUnit.SECONDS);
    }
}
