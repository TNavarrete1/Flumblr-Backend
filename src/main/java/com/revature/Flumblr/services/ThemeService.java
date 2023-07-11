package com.revature.Flumblr.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.Theme;
import com.revature.Flumblr.repositories.ThemeRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public Theme findByName(String name) {
        Optional<Theme> themeOpt = themeRepository.findByName(name);
        if (themeOpt.isEmpty())
            throw new ResourceNotFoundException("couldn't find theme " + name);
        return themeOpt.get();
    }

    public List<Theme> findAll() {
        List<Theme> themes = themeRepository.findAllByOrderByIdAsc();

        return themes;
    }
}
