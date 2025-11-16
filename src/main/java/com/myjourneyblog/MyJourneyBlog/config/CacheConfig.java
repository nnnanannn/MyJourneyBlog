package com.myjourneyblog.MyJourneyBlog.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache configuration using Caffeine
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configure Caffeine cache manager
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "posts",            // Cache for posts
                "users",            // Cache for user profiles
                "postsByTopic",     // Cache for topic queries
                "searchResults",    // Cache for search results
                "postsByTitle",     // Cache For findByTitle
                "githubCommits"     // Cache For GitHub sync data
        );

        cacheManager.setCaffeine(caffeineConfig());
        return cacheManager;
    }

    /**
     * Configure Caffeine cache properties
     */
    private Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .maximumSize(1000)                    // Max 1000 entries
                .expireAfterWrite(10, TimeUnit.MINUTES)  // Expire after 10 min
                .recordStats();                       // Enable statistics
    }
}
