package org.uetmydinh.appserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.uetmydinh.appserver.exception.UrlNotFoundException;
import org.uetmydinh.appserver.exception.UrlPersistenceException;
import org.uetmydinh.appserver.model.Url;
import org.uetmydinh.appserver.repository.UrlRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class UrlService {
    private static final Logger log = LoggerFactory.getLogger(UrlService.class);
    private final UrlRepository urlRepository;
    private final RedisTemplate<String, Url> redisTemplate;

    public UrlService(UrlRepository urlRepository, RedisTemplate<String, Url> redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
    }

    public String shortenUrl(String longUrl) {
        log.debug("Shortening URL: {}", longUrl);

        String id = generateShortKey();
        Url newUrl = new Url(id, longUrl, Instant.now(), Instant.now(), 0);

        try {
            urlRepository.save(newUrl);
            log.info("Successfully shortened URL {} to {}", longUrl, id);
            return id;
        } catch (Exception e) {
            log.error("Error saving URL {}", longUrl, e);
            throw new UrlPersistenceException("Unable to shorten URL " + longUrl, e);
        }
    }

    public String findOrigin(String id) {
        Url cachedUrl = redisTemplate.opsForValue().get(id);
        if (cachedUrl != null) {
            log.debug("Original URL for id {} found in cache: {}", id, cachedUrl);
            return cachedUrl.getLongUrl();
        }

        Url dbUrl = urlRepository.findById(id).orElseThrow(() -> new UrlNotFoundException(id));
        log.debug("Original URL for id {} found in database: {}", id, dbUrl);
        redisTemplate.opsForValue().set(id, dbUrl);
        return dbUrl.getLongUrl();
    }

    private String generateShortKey() {
        // TODO: Replace this logic with gRPC call when available
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
