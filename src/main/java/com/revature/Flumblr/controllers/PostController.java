package com.revature.Flumblr.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.revature.Flumblr.dtos.requests.NewCommentRequest;
import com.revature.Flumblr.services.PostServices;
import com.revature.Flumblr.services.TokenServices;

import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/update")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController {
    private final PostServices postService;
    private final TokenServices tokenService;
    @PostMapping("/post/comment")
    public ResponseEntity<?> likePost (@RequestBody NewCommentRequest req,  
    @RequestHeader("Authorization") String token) {

        tokenService.validateToken(token, req.getUser_id());
        postService.commentOnPost(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}