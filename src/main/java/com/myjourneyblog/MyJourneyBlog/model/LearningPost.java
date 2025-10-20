package com.myjourneyblog.MyJourneyBlog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String summary;

    private String topic;

    private String learningDate;

    private String author;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer viewCount = 0;

    private boolean published = true;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (learningDate == null) {
            learningDate = String.valueOf(LocalDate.now());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
