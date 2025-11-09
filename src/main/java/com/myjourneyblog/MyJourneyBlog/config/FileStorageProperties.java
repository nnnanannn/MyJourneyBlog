package com.myjourneyblog.MyJourneyBlog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for file storage
 */
@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {

    private String uploadDir;
    private String profileImagesDir;
    private String postImagesDir;
    private long maxProfileSize;
    private long maxPostSize;
    private String allowedExtensions;
}