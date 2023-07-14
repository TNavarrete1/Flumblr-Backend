package com.revature.Flumblr.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.Flumblr.entities.Theme;
import com.revature.Flumblr.services.ThemeService;

import lombok.AllArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/theme")
@AllArgsConstructor
public class ThemeController {
    ThemeService themeService;

    @GetMapping("/all")
    public ResponseEntity<List<Theme>> getThemes() {
        return ResponseEntity.status(HttpStatus.OK).body(themeService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Theme> getByName(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(themeService.findByName(name));
    }
}
