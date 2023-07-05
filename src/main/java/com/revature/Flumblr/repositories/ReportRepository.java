package com.revature.Flumblr.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The PostRepository interface provides database operations for Post entities.
 */
import com.revature.Flumblr.entities.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findAllBy(Pageable page);
    List<Report> findAllByUserId(String userId);
}
