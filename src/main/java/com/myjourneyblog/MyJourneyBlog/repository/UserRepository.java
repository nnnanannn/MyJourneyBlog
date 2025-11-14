package com.myjourneyblog.MyJourneyBlog.repository;

import com.myjourneyblog.MyJourneyBlog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    // ========== BASIC QUERIES ==========
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByResetToken(String resetToken);

    // ========== SEARCH QUERIES ==========
    // SQL: SELECT * FROM users WHERE username LIKE '%?%'
    // Use case: Search functionality
    List<User> findByUsernameContaining(String substring);
    List<User> findByFullnameContaining(String name);

    // ========== DATE-BASED QUERIES ==========
    // SQL: SELECT * FROM users WHERE created_at > ?
    // Use case: "New users this month"
    List<User> findByCreatedAtAfter(LocalDateTime date);
    // SQL: SELECT * FROM users WHERE created_at BETWEEN ? AND ?
    // Use case: "Users joined in date range"
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // ========== SORTING QUERIES ==========
    // SQL: SELECT * FROM users ORDER BY created_at DESC
    // Use case: "Latest users"
    List<User> findAllByOrderByCreatedAtDesc();
    // SQL: SELECT * FROM users WHERE username LIKE '?%' ORDER BY username ASC
    // Use case: Autocomplete functionality
    // OLD WAY: Method name query
    // List<User> findByUsernameStartingWithOrderByUsernameAsc(String prefix);
    // NEW WAY: Using @Query annotation (choose one)
    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT(:prefix, '%') ORDER BY u.username ASC")
    List<User> findByUsernameStartingWithOrderByUsernameAsc(@Param("prefix") String prefix);

    // ========== COUNT QUERIES ==========
    // SQL: SELECT COUNT(*) FROM users WHERE created_at > ?
    // Use case: Dashboard statistics
    long countByCreatedAtAfter(LocalDateTime date);

    // ========== CUSTOM @Query METHODS ==========
    // Find users with bio containing keyword (case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER (u.bio) LIKE LOWER(CONCAT('%',:keyword, '%'))")
    List<User> searhByBio(@Param("keyword") String keyword);

    // Find users created in last N days (N = number)
    @Query("SELECT u FROM User u WHERE u.createdAt >= :date ORDER BY u.createdAt DESC")
    List<User> findRecentUsers(@Param("date") LocalDateTime date);

    // Find users by username or email (useful for login)
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    // Count users registered today (native query example)
    @Query(value = "SELECT COUNT(*) FROM users WHERE DATE(created_at) = CURRENT_DATE", nativeQuery = true)
    long countUsersRegisteredToday();



}
