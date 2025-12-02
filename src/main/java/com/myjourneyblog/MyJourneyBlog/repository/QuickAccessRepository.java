package com.myjourneyblog.MyJourneyBlog.repository;

import com.myjourneyblog.MyJourneyBlog.model.QuickAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuickAccessRepository extends JpaRepository<QuickAccess, Long> {

    // Find by Post ID to check if specific post is added
    Optional<QuickAccess> findByPostId(Long postId);

    boolean existsByPostId(Long postId);

    // Get all ordered by most recently added
    List<QuickAccess> findAllByOrderByAddedAtDesc();
}