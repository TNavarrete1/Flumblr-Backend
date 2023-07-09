package com.revature.Flumblr.repositories;

import com.revature.Flumblr.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, String> {

    Theme findByName(String name);

}
