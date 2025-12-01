package com.myjourneyblog.MyJourneyBlog.repository;

import com.myjourneyblog.MyJourneyBlog.model.SiteSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing site settings
 */
@Repository
public interface SiteSettingRepository extends JpaRepository<SiteSetting, String> {

    // Basic CRUD is provided by JpaRepository.
    // The ID type is String because the primary key 'key' is a String.
}