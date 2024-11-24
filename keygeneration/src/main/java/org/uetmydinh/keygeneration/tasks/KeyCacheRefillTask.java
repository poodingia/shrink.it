package org.uetmydinh.keygeneration.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.uetmydinh.keygeneration.entity.Key;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class KeyCacheRefillTask implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(KeyCacheRefillTask.class);
    private final MongoTemplate mongoTemplate;
    private final Queue<Key> keyCache;
    private final int batchSize;

    public KeyCacheRefillTask(MongoTemplate mongoTemplate, Queue<Key> keyCache, int batchSize) {
        this.mongoTemplate = mongoTemplate;
        this.keyCache = keyCache;
        this.batchSize = batchSize;
    }

    public void run() {
        Query query = new Query(Criteria.where("isUsed").is(false)).limit(batchSize);
        Update update = new Update().set("isUsed", true);

        List<Key> keys = mongoTemplate.find(query, Key.class);
        if (!keys.isEmpty()) {
            List<String> keyIds = keys.stream().map(Key::getId).collect(Collectors.toList());
            mongoTemplate.updateMulti(new Query(Criteria.where("id").in(keyIds)), update, Key.class);
            keys.forEach(keyCache::offer);
            log.info("Successfully refilled {} keys into the cache", keys.size());
        } else {
            log.warn("No more keys available in the database");
        }
    }
}
