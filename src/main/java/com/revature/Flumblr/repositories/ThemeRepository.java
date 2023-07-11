package com.revature.Flumblr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.Flumblr.entities.Theme;
@Repository
public interface ThemeRepository extends JpaRepository<Theme, String> {
    Optional<Theme> findByName(String name);

    List<Theme> findAllByOrderByIdAsc();

    // Theme findByName(String name);

}
