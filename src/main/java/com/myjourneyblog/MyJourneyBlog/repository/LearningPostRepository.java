package com.myjourneyblog.MyJourneyBlog.repository;

import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningPostRepository extends JpaRepository<LearningPost, Long> {

    // Find by date

    // Find by topic

    // Find by author

    // Find all published posts

    // Find posts by date range

    // Get all unique topics

    // Get posts grouped by date
}
