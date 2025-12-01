package com.myjourneyblog.MyJourneyBlog.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity to store dynamic site configurations (e.g., About content, Contact info)
 */
@Entity
@Table(name = "site_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteSetting {

    @Id
    @Column(name = "setting_key", nullable = false, unique = true)
    private String key; // Identifier, e.g., "home_about_content"

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String value; // The actual content (HTML or text)

    // Helper method to update value easily
    public void update(String newValue) {
        this.value = newValue;
    }
}