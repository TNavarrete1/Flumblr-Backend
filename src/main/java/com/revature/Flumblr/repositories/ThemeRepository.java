package com.revature.Flumblr.repositories;

import com.revature.Flumblr.entities.Theme;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository {

    Theme getTheme(String themeName);

}
