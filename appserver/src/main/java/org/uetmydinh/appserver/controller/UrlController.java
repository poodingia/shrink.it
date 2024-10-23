package org.uetmydinh.appserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uetmydinh.appserver.dto.CreateUrlDTO;
import org.uetmydinh.appserver.service.UrlService;

@RestController
@RequestMapping("/api/urls")
public class UrlController {
    private final UrlService urlService;
    private static final Logger log = LoggerFactory.getLogger(UrlController.class);

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/")
    public ResponseEntity<String> shortenUrl(@RequestBody CreateUrlDTO url) {
        log.debug("Received request to shorten URL: {}", url.url());
        String shortenedUrl = urlService.shortenUrl(url.url());
        return ResponseEntity.ok(shortenedUrl);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findOrigin(@PathVariable String id) {
        log.debug("Received request to find original URL of: {}", id);
        String longUrl = urlService.findOrigin(id);
        return ResponseEntity.ok(longUrl);
    }
}
