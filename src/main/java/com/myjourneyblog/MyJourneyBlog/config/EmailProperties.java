package com.myjourneyblog.MyJourneyBlog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Email configuration properties
 */
@Configuration
@ConfigurationProperties(prefix = "email")
@Data
public class EmailProperties {

    private String from;
    private String fromName;
    private boolean enabled;
}