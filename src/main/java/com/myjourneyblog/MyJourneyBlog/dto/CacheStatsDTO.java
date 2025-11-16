package com.myjourneyblog.MyJourneyBlog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for cache statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheStatsDTO {

    private String cacheName;
    private long hitCount;
    private long missCount;
    private double hitRate;
    private long evictionCount;
    private long size;
}
