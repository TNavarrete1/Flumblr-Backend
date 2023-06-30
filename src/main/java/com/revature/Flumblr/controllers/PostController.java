package com.revature.Flumblr.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;

import com.revature.Flumblr.services.TokenServices;
import com.revature.Flumblr.services.PostService;
import com.revature.Flumblr.dtos.responses.PostResponse;

@RestController
@CrossOrigin
@RequestMapping("/posts")
public class PostController {
    // dependency injection ie. services
    private final TokenServices tokenService;
    private final PostService postService;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    public PostController(TokenServices tokenService, PostService postService) {
        this.tokenService = tokenService;
        this.postService = postService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostResponse>> getFeed(HttpServletRequest req) {
        // only users can create new tag
        String userId = tokenService.extractUserId(req.getHeader("auth-token")); 
        logger.trace("getting posts for user " + userId);
        // if library is not unique, throw exception
        return ResponseEntity.status(HttpStatus.OK).body(postService.getUserPosts(userId));
    }
}
