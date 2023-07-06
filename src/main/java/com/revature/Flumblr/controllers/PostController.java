package com.revature.Flumblr.controllers;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.Flumblr.services.TokenService;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;
import com.revature.Flumblr.services.PostService;

import com.revature.Flumblr.dtos.requests.NewCommentRequest;
import com.revature.Flumblr.dtos.responses.PostResponse;

import com.revature.Flumblr.entities.Post;

import com.revature.Flumblr.services.CommentService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/posts")
public class PostController {
    // dependency injection ie. services
    private final TokenService tokenService;
    private final PostService postService;
    private final CommentService commentService;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/feed/{page}")
    public ResponseEntity<List<PostResponse>> getFeed(@RequestHeader("Authorization") String token,
            @PathVariable int page) {
        if(page <= 0) throw new BadRequestException("page must be > 0");
        String userId = tokenService.extractUserId(token);
        logger.trace("generating feed for " + userId);
        List<Post> posts = postService.getFeed(userId, page - 1);
        List<PostResponse> resPosts = new ArrayList<PostResponse>();
        for (Post userPost : posts) {
            resPosts.add(new PostResponse(userPost));
        }
        return ResponseEntity.status(HttpStatus.OK).body(resPosts);
    }

    @GetMapping("/tag/{page}")
    public ResponseEntity<List<PostResponse>> getByTags(@RequestHeader("Authorization") String token,
            @PathVariable int page, @RequestParam List<String> tags) {
        if(page <= 0) throw new BadRequestException("page must be > 0");
        if(tags.size() < 1) throw new BadRequestException("empty tags parameter");
        String userId = tokenService.extractUserId(token);
        logger.trace("getting posts by tag(s) " + tags + " for " + userId);
        List<Post> posts = postService.findByTag(tags, page - 1);
        List<PostResponse> resPosts = new ArrayList<PostResponse>();
        for (Post userPost : posts) {
            resPosts.add(new PostResponse(userPost));
        }
        return ResponseEntity.status(HttpStatus.OK).body(resPosts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        logger.trace("getting posts from " + userId + " requested by " + requesterId);
        List<Post> userPosts = postService.getUserPosts(userId);
        List<PostResponse> resPosts = new ArrayList<PostResponse>();
        for (Post userPost : userPosts) {
            resPosts.add(new PostResponse(userPost));
        }
        return ResponseEntity.status(HttpStatus.OK).body(resPosts);
    }

    @GetMapping("/id/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String postId,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        logger.trace("getting post " + postId + " requested by " + requesterId);
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponse(postService.getPost(postId)));
    }

    @PostMapping("/comment")
    public ResponseEntity<?> commentOnPost(@RequestBody NewCommentRequest req,
            @RequestHeader("Authorization") String token) {

        tokenService.validateToken(token, req.getUser_id());
        commentService.commentOnPost(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/trending/{fromDate}/{userId}")
    public ResponseEntity<List<PostResponse>> 
    getTrending (
        @PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
        @PathVariable("userId") String userId,
        @RequestHeader("Authorization") String token)  
    {
         tokenService.validateToken(token, userId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getTrending(fromDate));
    }

    

}
