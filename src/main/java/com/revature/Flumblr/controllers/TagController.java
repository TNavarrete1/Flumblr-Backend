package com.revature.Flumblr.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.Flumblr.services.TagService;
import com.revature.Flumblr.services.TokenService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/tags")
public class TagController {
    // dependency injection ie. services
    private final TokenService tokenService;
    private final TagService tagService;
    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/trending/{fromDate}")
    public ResponseEntity<List<String>> getTrending(
            @PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        logger.trace("getting trending for requester: " + requesterId);
        return ResponseEntity.status(HttpStatus.OK).body(tagService.getTrending(fromDate));
    }

}
