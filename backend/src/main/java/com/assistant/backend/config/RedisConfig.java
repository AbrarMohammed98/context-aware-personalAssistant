package com.assistant.backend.config;

import com.assistant.backend.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@SuppressWarnings("deprecation")
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        Jackson2JsonRedisSerializer<User> userSerializer =
                new Jackson2JsonRedisSerializer<>(User.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        userSerializer.setObjectMapper(mapper);

        RedisCacheConfiguration userCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(userSerializer));

        return RedisCacheManager.builder(connectionFactory)
                .withCacheConfiguration("users", userCacheConfig)
                .build();
    }
}