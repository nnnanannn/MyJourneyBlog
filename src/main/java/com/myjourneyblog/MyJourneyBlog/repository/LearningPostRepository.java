package com.myjourneyblog.MyJourneyBlog.repository;

import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LearningPostRepository extends JpaRepository<LearningPost, Long>{

    // Find all posts by specific author
    List<LearningPost> findByAuthorId(Long authorId);

    // Find posts by category
    List<LearningPost> findByCategory(String category);

    // Search posts by title
    List<LearningPost> findByTitleContaining(String keyword);

    // Find recent posts
    List<LearningPost> findByCreatedAtAfter(LocalDateTime date);

    // Find posts ordered by creation date
    List<LearningPost> findAllByOrderByCreatedAtDesc();

    // Find posts by author username
    List<LearningPost> findByAuthorUsername(String username);

    // ========== CUSTOM @Query METHODS ==========


    // Find all posts with author (prevents N+1)
    @Query("SELECT p FROM LearningPost p JOIN FETCH p.author")
    List<LearningPost> findAllWithAuthors();

    // Find posts by author with JOIN FETCH
    @Query("SELECT p FROM LearningPost p JOIN FETCH p.author WHERE p.author.id = :authorId")
    List<LearningPost> findByAuthorIdWithAuthor(@Param("authorId") Long authorId);

    // Find posts by category with author
    @Query("SELECT p FROM LearningPost p JOIN FETCH p.author WHERE p.category = :category")
    List<LearningPost> findByCategoryWithAuthor(@Param("category") String category);

    // Search in title or content
    @Query("SELECT p FROM LearningPost p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<LearningPost> searchByTitleOrContent(@Param("keyword") String keyword);

    // Count posts by author
    long countByAuthorId(Long authorId);

    // Count posts by category
    long countByCategory(String category);
}
