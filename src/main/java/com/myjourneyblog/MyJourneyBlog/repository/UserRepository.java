package com.myjourneyblog.MyJourneyBlog.repository;

import com.myjourneyblog.MyJourneyBlog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    // Basic queries
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Search queries
    // SQL: SELECT * FROM users WHERE username LIKE '%?%'
    // Use case: Search functionality
    List<User> findByUsernameContaining(String substring);
    List<User> findByFullNameContaining(String name);

    // Data-based queries
    // SQL: SELECT * FROM users WHERE created_at > ?
    // Use case: "New users this month"
    List<User> findByCreatedAtAfter(LocalDateTime date);
    // SQL: SELECT * FROM users WHERE created_at BETWEEN ? AND ?
    // Use case: "Users joined in date range"
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Sorting queries
    // SQL: SELECT * FROM users ORDER BY created_at DESC
    // Use case: "Latest users"
    List<User> findAllByOrderByCreatedAtDesc();
    // SQL: SELECT * FROM users WHERE username LIKE '?%' ORDER BY username ASC
    // Use case: Autocomplete functionality
    List<User> findByUsernameStartingWithOrderByUsernameAsc(String prefix);

    // Count queries
    // SQL: SELECT COUNT(*) FROM users WHERE created_at > ?
    // Use case: Dashboard statistics
    long countByCreatedAtAfter(LocalDateTime date);
}
