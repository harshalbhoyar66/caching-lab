package com.cachinglab.caching_lab.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class L1CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {

        return Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .recordStats();
    }
}