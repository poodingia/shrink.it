package org.uetmydinh.appserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.uetmydinh.appserver.exception.UrlNotFoundException;
import org.uetmydinh.appserver.exception.UrlPersistenceException;
import org.uetmydinh.appserver.model.Url;
import org.uetmydinh.appserver.repository.UrlRepository;
import org.uetmydinh.lib.KeyGenerationRequest;
import org.uetmydinh.lib.KeyGenerationServiceGrpc.KeyGenerationServiceBlockingStub;
import net.devh.boot.grpc.client.inject.GrpcClient;

import java.time.Instant;

@Service
public class UrlService {
    private static final Logger log = LoggerFactory.getLogger(UrlService.class);
    private final UrlRepository urlRepository;
    private final RedisTemplate<String, Url> redisTemplate;

    @GrpcClient("keyGenerationService")
    private KeyGenerationServiceBlockingStub keyGenerationServiceBlockingStub;

    public UrlService(UrlRepository urlRepository,
                      RedisTemplate<String, Url> redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
    }

    public String shortenUrl(String longUrl) {
        String id = generateShortKey();
        log.debug("Generated key: {}", id);
        Url newUrl = new Url(id, longUrl, Instant.now(), Instant.now(), 0);

        try {
            urlRepository.save(newUrl);
            return id;
        } catch (Exception e) {
            throw new UrlPersistenceException("Unable to shorten URL " + longUrl, e);
        }
    }

    public String findOrigin(String id) {
        Url cachedUrl = redisTemplate.opsForValue().get(id);
        if (cachedUrl != null) {
            log.debug("Cache hit: {}", id);
            return cachedUrl.getLongUrl();
        }

        log.debug("Cache miss: {}", id);
        Url dbUrl = urlRepository.findById(id).orElseThrow(() -> new UrlNotFoundException(id));
        redisTemplate.opsForValue().set(id, dbUrl);
        return dbUrl.getLongUrl();
    }

    private String generateShortKey() {
        KeyGenerationRequest request = KeyGenerationRequest.newBuilder().build();
        return keyGenerationServiceBlockingStub.generateKey(request).getKey();
    }
}