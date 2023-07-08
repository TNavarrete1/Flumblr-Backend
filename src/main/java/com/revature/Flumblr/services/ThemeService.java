package com.revature.Flumblr.services;

import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.Theme;
import com.revature.Flumblr.repositories.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ThemeService {

    ThemeRepository themeRepository;

    public Theme getTheme(Profile profile) {
        return themeRepository.findByProfile(profile);
    }

}
