package com.revature.Flumblr.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.Flumblr.dtos.requests.CommentVoteRequest;
import com.revature.Flumblr.dtos.requests.PostVoteRequest;
import com.revature.Flumblr.dtos.requests.ProfileVoteRequest;
import com.revature.Flumblr.services.CommentVoteService;
import com.revature.Flumblr.services.PostVoteService;
import com.revature.Flumblr.services.ProfileVoteService;
import com.revature.Flumblr.services.TokenService;

import lombok.AllArgsConstructor;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/vote")
public class VoteController {
    private final TokenService tokenService;
    private final PostVoteService postVoteService;
    private final CommentVoteService commentVoteService;
    private final ProfileVoteService profileVoteService;

    @PostMapping("/post")
    public ResponseEntity<?> votePost(@RequestBody PostVoteRequest req,
            @RequestHeader("Authorization") String token) {
        tokenService.validateToken(token, req.getUserId());

        postVoteService.vote(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comment")
    public ResponseEntity<?> voteComment(@RequestBody CommentVoteRequest req,
            @RequestHeader("Authorization") String token) {
        tokenService.validateToken(token, req.getUserId());

        commentVoteService.vote(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/profile")
    public ResponseEntity<?> voteProfile(@RequestBody ProfileVoteRequest req,
            @RequestHeader("Authorization") String token) {
        tokenService.validateToken(token, req.getUserId());

        profileVoteService.vote(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
