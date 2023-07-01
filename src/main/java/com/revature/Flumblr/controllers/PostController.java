package com.revature.Flumblr.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.PathVariable;

import com.revature.Flumblr.services.TokenService;
import com.revature.Flumblr.services.PostService;
import com.revature.Flumblr.dtos.responses.PostResponse;

@RestController
@CrossOrigin
@RequestMapping("/posts")
public class PostController {
    // dependency injection ie. services
    private final TokenService tokenService;
    private final PostService postService;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    public PostController(TokenService tokenService, PostService postService) {
        this.tokenService = tokenService;
        this.postService = postService;
    }

    @GetMapping("/feed/{page}")
    public ResponseEntity<List<PostResponse>> getFeed(@RequestHeader("Authorization") String token,
    @PathVariable int page) {
        String userId = tokenService.extractUserId(token);
        logger.trace("generating feed for " + userId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getFeed(userId, page - 1));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable String userId,
    @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token); 
        logger.trace("getting posts from " + userId + " requested by " + requesterId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getUserPosts(userId));
    }

    @GetMapping("/id/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String postId,
    @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token); 
        logger.trace("getting post " + postId + " requested by " + requesterId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }
}
