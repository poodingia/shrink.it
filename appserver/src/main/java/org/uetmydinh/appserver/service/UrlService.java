package org.uetmydinh.appserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
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
        return urlRepository.findById(id)
                .map(url -> {
                    log.debug("Original URL for id {} found: {}", id, url.getLongUrl());
                    return url.getLongUrl();
                })
                .orElseThrow(() -> {
                    log.warn("Original URL not found for id: {}", id);
                    return new UrlNotFoundException(id);
                });
    }

    private String generateShortKey() {
        // TODO: Replace this logic with gRPC call when available
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
