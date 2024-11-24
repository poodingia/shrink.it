package org.uetmydinh.appserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.uetmydinh.appserver.model.Url;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofDays(1))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration("shortUrlCache", myDefaultCacheConfig(Duration.ofDays(1)))
                .build();
    }

    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisTemplate<String, Url> redisTemplate() {
        RedisTemplate<String, Url> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Jackson2JsonRedisSerializer<Url> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Url.class);
        template.setValueSerializer(serializer);
        return template;
    }

}
