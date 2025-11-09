package com.myjourneyblog.MyJourneyBlog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configure static resource handlers for uploaded files
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files as static resources
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
