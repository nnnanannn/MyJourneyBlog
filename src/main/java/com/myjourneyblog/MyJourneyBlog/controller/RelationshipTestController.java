package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.LearningPostRepository;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
