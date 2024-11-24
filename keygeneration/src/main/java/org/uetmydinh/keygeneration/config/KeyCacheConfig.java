package org.uetmydinh.keygeneration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uetmydinh.keygeneration.entity.Key;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
public class KeyCacheConfig {
    @Value("${keycache.enabled:true}")
    private boolean enabled;

    @Value("${keycache.initialBatchSize:2000}")
    private int initialBatchSize;

    @Value("${keycache.refillInterval:60}")
    private int refillInterval;

    @Value("${keycache.minBatchSize:100}")
    private int minBatchSize;

    @Value("${keycache.maxBatchSize:10000}")
    private int maxBatchSize;

    @Bean
    public boolean enabled() {
        return enabled;
    }

    @Bean
    public Queue<Key> keyCache() {
        return new ConcurrentLinkedQueue<>();
    }

    @Bean
    public int initialBatchSize() {
        return initialBatchSize;
    }

    @Bean
    public int refillInterval() {
        return refillInterval;
    }

    @Bean
    public int minBatchSize() {
        return minBatchSize;
    }

    @Bean
    public int maxBatchSize() {
        return maxBatchSize;
    }

}
