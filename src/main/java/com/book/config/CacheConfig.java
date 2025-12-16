package com.book.config;

import com.book.constant.CommonConstant;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {


    @Bean
    public CacheManager cacheManager() {

        SimpleCacheManager cacheManager = new SimpleCacheManager();


        List<CaffeineCache> caches = new ArrayList<>();

        Cache<Object, Object> codeCache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
        caches.add(new CaffeineCache(CommonConstant.CACHE_KEY_CODE, codeCache));

        Cache<Object, Object> bookCache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
        caches.add(new CaffeineCache(CommonConstant.CACHE_KEY_BOOK, bookCache));


        Cache<Object, Object> userCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
        caches.add(new CaffeineCache(CommonConstant.CACHE_KEY_USER, userCache));



        // 5. 将自定义缓存注入管理器
        cacheManager.setCaches(caches);

        return cacheManager;
    }
}