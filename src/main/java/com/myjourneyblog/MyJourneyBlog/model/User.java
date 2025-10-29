package com.myjourneyblog.MyJourneyBlog.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter // Separate instead of @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min =3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min =6, message = "Password must be at least 6 characters" )
    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "full_name")
    private String fullname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "github_username")
    private String githubUsername;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    // ========== RELATIONSHIP TO LEARNING POSTS ==========

    @OneToMany(
            mappedBy = "author",  // Field name in LearningPost
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<LearningPost> learningPosts = new ArrayList<>();

    // ========== TIMESTAMPS ==========

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // @Prepersist makes method called automatically before entity is updated
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ========== CONVENIENCE METHODS ==========

    /**
     * Add learning post to user's collection
     * Maintains bidirectional relationship
     */
    public void addLearningPost(LearningPost post) {
        learningPosts.add(post);
        post.setAuthor(this);
    }

    /**
     * Remove learning post from user's collection
     * Maintains bidirectional relationship
     */
    public void removeLearningPost(LearningPost post) {
        learningPosts.remove(post);
        post.setAuthor(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<ProjectUpdate> projectUpdates = new ArrayList<>();

// Add convenience methods:

    public void addProjectUpdate(ProjectUpdate update) {
        projectUpdates.add(update);
        update.setAuthor(this);
    }

    public void removeProjectUpdate(ProjectUpdate update) {
        projectUpdates.remove(update);
        update.setAuthor(null);
    }
}