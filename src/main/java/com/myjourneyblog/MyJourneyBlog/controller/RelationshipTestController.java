package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.LearningPostRepository;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test/relationships")
@RequiredArgsConstructor
public class RelationshipTestController {

    private final UserRepository userRepository;
    private final LearningPostRepository learningPostRepository;

    // ========== CREATE TEST DATA ==========

    @PostMapping("/setup")
    @Transactional
    public String setupTestData() {
        // Create user
        User user = User.builder()
                .username("learner")
                .email("learner@example.com")
                .password("password123")
                .fullname("Active Learner")
                .build();

        User savedUser = userRepository.save(user);

        // Create posts for user
        LearningPost post1 = LearningPost.builder()
                .title("Understanding Spring Boot")
                .content("Today I learned how Spring Boot auto-configuration works...")
                .keyTakeaways("Auto-configuration magic!")
                .category("SPRING")
                .author(savedUser)
                .build();

        LearningPost post2 = LearningPost.builder()
                .title("JPA Relationships Deep Dive")
                .content("Explored @ManyToOne and @OneToMany relationships...")
                .keyTakeaways("Bidirectional relationships require careful management")
                .category("JPA")
                .author(savedUser)
                .build();

        learningPostRepository.save(post1);
        learningPostRepository.save(post2);

        return "Created 1 user with 2 posts";
    }

    // ========== TEST QUERIES ==========

    @GetMapping("/user-posts/{userID}")
    @Transactional(readOnly = true)
    public List<LearningPost> getUserPosts(@PathVariable Long userID) {
        User user = userRepository.findById(userID).orElseThrow();
        return user.getLearningPosts(); // Tests LAZY loading
    }

    @GetMapping("/post-author/{postId}")
    @Transactional(readOnly = true)
    public String getPostAuthor(@PathVariable Long postId) {
        LearningPost post = learningPostRepository.findById(postId).orElseThrow();
        return "Post by: " + post.getAuthor().getUsername(); // Tests LAZY loading
    }

    @GetMapping("/posts-by-author/{authorId}")
    public List<LearningPost> getPostsByAuthor(@PathVariable Long authorId) {
        return learningPostRepository.findByAuthorId(authorId);
    }

    @GetMapping("/posts-by-category/{category}")
    public List<LearningPost> getPostsByCategory(@PathVariable String category) {
        return learningPostRepository.findByCategory(category);
    }

    @GetMapping("/all-posts-with-authors")
    public List<LearningPost> getAllPostsWithAuthors() {
        return learningPostRepository.findAllWithAuthors(); // Tests JOIN FETCH
    }

    @GetMapping("/search/{keyword}")
    public List<LearningPost> searchPosts(@PathVariable String keyword) {
        return learningPostRepository.searchByTitleOrContent(keyword);
    }

    // ========== TEST CASCADE DELETE ==========

    @DeleteMapping("/delete-user-cascade/{userId}")
    @Transactional
    public String deleteUserCascade(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        int postCount = user.getLearningPosts().size();
        // Call setter of learningPosts field in User Entity

        userRepository.delete(user);

        return String.format("Deleted user and %d posts (cascade)", postCount);
    }

    // ========== TEST ORPHAN REMOVAL ==========

    @DeleteMapping("/remove-post-orphan/{userId}/{postId}")
    @Transactional
    public String removePostOrphan(@PathVariable Long userId, @PathVariable Long postId) {
        User user = userRepository.findById(userId).orElseThrow();
        LearningPost post = learningPostRepository.findById(postId).orElseThrow();

        user.removeLearningPost(post); // Uses convenience method

        return "Post removed from user (orphan removal should delete it)";
    }

    // ========== STATISTICS ==========

    @GetMapping("/stats/user/{userId}")
    public String getUserStats(@PathVariable Long userId) {
        long postCount = learningPostRepository.countByAuthorId(userId);
        return String.format("User has %d posts", postCount);
    }

}
