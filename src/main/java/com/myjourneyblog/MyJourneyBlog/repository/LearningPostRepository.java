package com.myjourneyblog.MyJourneyBlog.repository;

import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LearningPostRepository extends JpaRepository<LearningPost, Long>{

    // Find by date
    List<LearningPost> findByLearningDateOrderByCreatedAtDesc(LocalDate learningDate);

    // Find by topic
    List<LearningPost> findByTopicOrderByLearningDateDesc(String topic);

    // Find by author
    List<LearningPost> findByAuthorIdOrderByLearningDateDesc(Long authorId);

    // Custom query with @Query
    @Query("SELECT DISTINCT l.topic FROM LearningPost l ORDER BY l.topic")
    List<String> findDistinctTopics();
}
