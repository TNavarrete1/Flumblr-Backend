package com.revature.Flumblr.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.revature.Flumblr.entities.Theme;
import com.revature.Flumblr.services.ThemeService;

public class ThemeControllerTest {
    @Mock
    ThemeService themeService;

    @InjectMocks
    ThemeController themeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByName() {
        String name = "default";
        Theme theme = new Theme();
        theme.setName(name);

        when(themeService.findByName(name)).thenReturn(theme);
        ResponseEntity<Theme> response = themeController.getByName(name);
        assertEquals(theme, response.getBody());
    }

    @Test
    void testGetThemes() {
        List<Theme> themes = new ArrayList<>();
        when(themeService.findAll()).thenReturn(themes);

        ResponseEntity<List<Theme>> response = themeController.getThemes();
        assertEquals(themes, response.getBody());
    }
}
