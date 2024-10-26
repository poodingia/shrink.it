package org.uetmydinh.appserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uetmydinh.appserver.dto.CreateUrlDTO;
import org.uetmydinh.appserver.service.UrlService;

@RestController
@RequestMapping("/api/urls")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/")
    public ResponseEntity<String> shortenUrl(@RequestBody CreateUrlDTO url) {
        String shortenedUrl = urlService.shortenUrl(url.url());
        return ResponseEntity.ok(shortenedUrl);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findOrigin(@PathVariable String id) {
        String longUrl = urlService.findOrigin(id);
        return ResponseEntity.ok(longUrl);
    }
}
