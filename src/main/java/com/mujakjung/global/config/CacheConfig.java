package com.mujakjung.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    /** 공통 Caffeine 스펙 */
    private Caffeine<Object, Object> caffeineSpec() {
        return Caffeine.newBuilder()
                .initialCapacity(9)
                .maximumSize(9)
                .expireAfterWrite(60, TimeUnit.SECONDS)   // TTL = 60s
                .recordStats();
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("hot");
        manager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(9)
                        .expireAfterWrite(60, TimeUnit.SECONDS)
                        .recordStats()
        );
        manager.setAllowNullValues(false);
        return manager;
    }
}
