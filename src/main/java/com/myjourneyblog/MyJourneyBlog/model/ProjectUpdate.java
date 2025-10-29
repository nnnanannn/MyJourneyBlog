package com.myjourneyblog.MyJourneyBlog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Entity representing updates to coding projects
 */
@Entity
@Table(name = "project_updates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200)
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "project_name")
    private String projectName;

    @Enumerated(EnumType.STRING)
    @Column(name = "update_type")
    private UpdateType updateType;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @Column(name = "github_repo_url")
    private String githubRepoUrl;

    @Column(name = "github_commit_hash")
    private String githubCommitHash;

    @Column(name = "github_pr_number")
    private Integer githubPrNumber;

    // Store complex GitHub PR data as JSON
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "github_pr_data", columnDefinition = "jsonb")
    private String githubPrData;

    @Column(columnDefinition = "TEXT")
    private String technologiesUsed;

    @Column(columnDefinition = "TEXT")
    private String challengesFaced;

    @Column(columnDefinition = "TEXT")
    private String lessonsLearned;

    @Column(columnDefinition = "TEXT")
    private String nextSteps;

    // ========== RELATIONSHIP TO USER ==========

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    // ========== TIMESTAMPS ==========

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

}
