package com.myjourneyblog.MyJourneyBlog.model;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.repository.LearningPostRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Post title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, message = "Content must be at least 10 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String keyTakeaways;

    private String category; // JAVA, SPRING, DATABASE, etc.

    @Column(name = "resources_used", columnDefinition = "TEXT")
    private String resourcesUsed;

    // ========== RELATIONSHIP TO USER ==========

    // Many posts belong to one user,
    // Do not load user unless needed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    // ========== TIMESTAMPS ==========

    @Column(name = "learning_date")
    private LocalDate learningDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Column(name = "view_count", nullable = true)
    private Integer viewCount = 0;

    // Helper method to increment view count
    public void incrementViewCount() {
        this.viewCount++;
    }

    // Prevent negative view counts
    //@PrePersist
    //@PreUpdate
    private void validateViewCount() {
        if (this.viewCount == null) {
            this.viewCount = 0;
        }
        if (this.viewCount < 0) {
            this.viewCount = 0;
        }
    }
}
