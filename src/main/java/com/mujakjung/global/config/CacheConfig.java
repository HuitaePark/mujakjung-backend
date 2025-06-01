package com.mujakjung.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    /** 공통 Caffeine 스펙 */
    private Caffeine<Object, Object> caffeineSpec() {
        return Caffeine.newBuilder()
                .initialCapacity(9)        // 예상 엔트리 수
                .maximumSize(9)            // 유형 3개 × 3배
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .refreshAfterWrite(25, TimeUnit.SECONDS)
                .recordStats();            // hit/miss 모니터링
    }

    @Bean
    public CacheManager cacheManager() {
        // 캐시 이름 정의
        CaffeineCacheManager manager = new CaffeineCacheManager("hotCourse", "hotRestaurant", "hotAccommodation");

        manager.setAllowNullValues(false);      // null 저장 허용 안 함 권장
        manager.setCaffeine(caffeineSpec());    // 공통 스펙 적용
        return manager;
    }
}
