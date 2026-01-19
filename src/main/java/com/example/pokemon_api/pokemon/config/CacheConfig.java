package com.example.pokemon_api.pokemon.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {

        CaffeineCache typeCache = new CaffeineCache(
                "types",
                Caffeine.newBuilder()
                        .expireAfterWrite(24, TimeUnit.HOURS)
                        .maximumSize(100)
                        .build()
        );

        CaffeineCache abilityCache = new CaffeineCache(
                "abilities",
                Caffeine.newBuilder()
                        .expireAfterWrite(24, TimeUnit.HOURS)
                        .maximumSize(500)
                        .build()
        );

        return new SimpleCacheManager() {{
            setCaches(List.of(typeCache, abilityCache));
        }};
    }
}
