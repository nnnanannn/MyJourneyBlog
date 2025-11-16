package com.myjourneyblog.MyJourneyBlog.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.myjourneyblog.MyJourneyBlog.dto.CacheStatsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST API for cache management and statistics
 */
@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cache Management", description = "Cache statistics and management")
public class CacheController {

    private final CacheManager cacheManager;

    /**
     * Get statistics for all caches
     */
    @GetMapping("/stats")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get cache statistics",
            description = "View hit rates, miss rates, and performance metrics",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<CacheStatsDTO>> getCacheStats() {
        List<CacheStatsDTO> statsList = new ArrayList<>();

        cacheManager.getCacheNames().forEach(cacheName -> {
            org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);

            if (cache instanceof CaffeineCache) {
                CaffeineCache caffeineCache = (CaffeineCache) cache;
                Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                CacheStats stats = nativeCache.stats();

                long size = nativeCache.estimatedSize();

                CacheStatsDTO dto = CacheStatsDTO.builder()
                        .cacheName(cacheName)
                        .hitCount(stats.hitCount())
                        .missCount(stats.missCount())
                        .hitRate(stats.hitRate())
                        .evictionCount(stats.evictionCount())
                        .size(size)
                        .build();

                statsList.add(dto);
            }
        });

        return ResponseEntity.ok(statsList);
    }

    /**
     * Clear specific cache
     */
    @DeleteMapping("/{cacheName}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Clear specific cache",
            description = "Remove all entries from a cache",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> clearCache(@PathVariable String cacheName) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);

        if (cache != null) {
            cache.clear();
            log.info("Cache cleared: {}", cacheName);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Clear all caches
     */
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Clear all caches",
            description = "Remove all entries from all caches",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });

        log.info("All caches cleared");
        return ResponseEntity.noContent().build();
    }
}
