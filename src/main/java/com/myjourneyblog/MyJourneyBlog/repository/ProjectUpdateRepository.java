package com.myjourneyblog.MyJourneyBlog.repository;

import com.myjourneyblog.MyJourneyBlog.model.ProjectStatus;
import com.myjourneyblog.MyJourneyBlog.model.ProjectUpdate;
import com.myjourneyblog.MyJourneyBlog.model.UpdateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectUpdateRepository extends JpaRepository<ProjectUpdate, Long> {

    // ========== BASIC QUERIES ==========

    List<ProjectUpdate> findByAuthorId(Long authorId);

    List<ProjectUpdate> findByAuthorUsername(String username);

    List<ProjectUpdate> findByProjectName(String projectName);

    // ========== ENUM QUERIES ==========

    List<ProjectUpdate> findByUpdateType(UpdateType updateType);

    List<ProjectUpdate> findByProjectStatus(ProjectStatus status);

    List<ProjectUpdate> findByAuthorIdAndUpdateType(Long authorId, UpdateType updateType);

    List<ProjectUpdate> findByAuthorIdAndProjectStatus(Long authorId, ProjectStatus status);

    // ========== SEARCH QUERIES ==========

    List<ProjectUpdate> findByTitleContaining(String keyword);

    List<ProjectUpdate> findByProjectNameContaining(String projectName);

    // ========== DATE QUERIES ==========

    List<ProjectUpdate> findByCreatedAtAfter(LocalDateTime date);

    List<ProjectUpdate> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // ========== SORTING QUERIES ==========

    List<ProjectUpdate> findAllByOrderByCreatedAtDesc();

    List<ProjectUpdate> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    List<ProjectUpdate> findByProjectStatusOrderByUpdatedAtDesc(ProjectStatus status);

    // ========== COUNT QUERIES ==========

    long countByAuthorId(Long authorId);

    long countByProjectStatus(ProjectStatus status);

    long countByUpdateType(UpdateType updateType);

    long countByAuthorIdAndProjectStatus(Long authorId, ProjectStatus status);

    // ========== CUSTOM @Query METHODS ==========

    // Find all with authors (prevent N+1)
    @Query("SELECT p FROM ProjectUpdate p JOIN FETCH p.author")
    List<ProjectUpdate> findAllWithAuthors();

    // Find by author with JOIN FETCH
    @Query("SELECT p FROM ProjectUpdate p JOIN FETCH p.author WHERE p.author.id = :authorId")
    List<ProjectUpdate> findByAuthorIdWithAuthor(@Param("authorId") Long authorId);

    // Search in title or description
    @Query("SELECT p FROM ProjectUpdate p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProjectUpdate> searchByTitleOrDescription(@Param("keyword") String keyword);

    // Find updates with GitHub PRs
    @Query("SELECT p FROM ProjectUpdate p WHERE p.githubPrNumber IS NOT NULL")
    List<ProjectUpdate> findUpdatesWithGitHubPR();

    // Find recent updates by project name
    @Query("SELECT p FROM ProjectUpdate p WHERE p.projectName = :projectName " +
            "AND p.createdAt >= :since ORDER BY p.createdAt DESC")
    List<ProjectUpdate> findRecentByProjectName(
            @Param("projectName") String projectName,
            @Param("since") LocalDateTime since
    );

    // Find most active projects
    @Query("SELECT p.projectName, COUNT(p) as updateCount FROM ProjectUpdate p " +
            "WHERE p.projectName IS NOT NULL " +
            "GROUP BY p.projectName " +
            "ORDER BY updateCount DESC")
    List<Object[]> findMostActiveProjects();

    // Native query for JSON operations (PostgreSQL specific)
    @Query(value = "SELECT * FROM project_updates WHERE github_pr_data IS NOT NULL", nativeQuery = true)
    List<ProjectUpdate> findAllWithGitHubData();
}