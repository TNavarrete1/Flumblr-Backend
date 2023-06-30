package com.revature.Flumblr.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable String userId, HttpServletRequest req) {
        String requesterId = tokenService.extractUserId(req.getHeader("auth-token")); 
        logger.trace("getting posts from " + userId + " requested by " + requesterId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getUserPosts(userId));
    }

    @GetMapping("/id/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String postId, HttpServletRequest req) {
        String requesterId = tokenService.extractUserId(req.getHeader("auth-token")); 
        logger.trace("getting post " + postId + " requested by " + requesterId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }
}
