package com.web.redis.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }


    /*
    Custom LettuceConnectionFactory via LettuceClientConfiguration
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {

      LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        .useSsl().and()
        .commandTimeout(Duration.ofSeconds(2))
        .shutdownTimeout(Duration.ZERO)
        .build();

      return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379), clientConfig);
    }
    */

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration =
                myRedisCacheConfiguration(Duration.ofMinutes(5))
                        .disableCachingNullValues();
        // Why do we need .cacheDefaults() => to prevent wasting space
        // Because If we don't have, another cache will don't have ttl
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withCacheConfiguration("product", myRedisCacheConfiguration(Duration.ofMinutes(1)))
                // prefix-key name/ cache name: product ttl = 1 minutes
                .build();
    }

    private RedisCacheConfiguration myRedisCacheConfiguration(Duration timeToLive) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
                // to change structure from byte => json. we can use them without Serializable
    }
}

