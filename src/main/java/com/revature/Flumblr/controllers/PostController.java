package com.revature.Flumblr.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.revature.Flumblr.services.TokenService;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;
import com.revature.Flumblr.services.PostService;
import com.revature.Flumblr.services.S3StorageService;
import com.revature.Flumblr.services.PostShareService;
import com.revature.Flumblr.dtos.requests.NewCommentRequest;
import com.revature.Flumblr.dtos.responses.PostResponse;

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
    private final PostShareService postShareService;
    private final S3StorageService s3StorageService;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/create")
    public ResponseEntity<?> createPost(MultipartHttpServletRequest req, @RequestHeader("Authorization") String token) {

        String userId = tokenService.extractUserId(token);
        logger.trace("creating post from " + userId);

        MultipartFile file = req.getFile("file");
        String fileUrl = null;

        if (file != null) {
            fileUrl = s3StorageService.uploadFile(file);
        }

        postService.createPost(req, fileUrl, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/share/{postId}")
    public ResponseEntity<?> sharePost(@RequestHeader("Authorization") String token,
        @PathVariable String postId) {
        String userId = tokenService.extractUserId(token);

        postShareService.create(postId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/share/{postId}")
    public ResponseEntity<?> unSharePost(@RequestHeader("Authorization") String token,
        @PathVariable String postId) {
        String userId = tokenService.extractUserId(token);

        postShareService.delete(postId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/feed/{page}")
    public ResponseEntity<List<PostResponse>> getFeed(@RequestHeader("Authorization") String token,
            @PathVariable int page) {
        if (page <= 0)
            throw new BadRequestException("page must be > 0");
        String requesterId = tokenService.extractUserId(token);
        logger.trace("generating feed for " + requesterId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getFeed(page - 1, requesterId));
    }

    @GetMapping("/following/{page}")
    public ResponseEntity<List<PostResponse>> getFollowing(@RequestHeader("Authorization") String token,
            @PathVariable int page) {
        if (page <= 0)
            throw new BadRequestException("page must be > 0");
        String requesterId = tokenService.extractUserId(token);
        logger.trace("generating feed for " + requesterId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getFollowing(requesterId, page - 1));
    }

    @GetMapping("/tag/{page}")
    public ResponseEntity<List<PostResponse>> getByTags(@RequestHeader("Authorization") String token,
            @PathVariable int page, @RequestParam List<String> tags) {
        if (page <= 0)
            throw new BadRequestException("page must be > 0");
        if (tags.size() < 1)
            throw new BadRequestException("empty tags parameter");
        String requesterId = tokenService.extractUserId(token);
        logger.trace("getting posts by tag(s) " + tags + " for " + requesterId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.findByTag(tags, page - 1, requesterId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        logger.trace("getting posts from " + userId + " requested by " + requesterId);

        return ResponseEntity.status(HttpStatus.OK).body(postService.getUserPosts(userId, requesterId));
    }

    @GetMapping("/id/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String postId,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        return ResponseEntity.status(HttpStatus.OK).body(postService.findByIdResponse(postId, requesterId));
    }

    @PostMapping("/comment")
    public ResponseEntity<?> commentOnPost(@RequestBody NewCommentRequest req,
            @RequestHeader("Authorization") String token) {

        tokenService.validateToken(token, req.getUserId());
        commentService.commentOnPost(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        String role = tokenService.extractRole(token);

        logger.trace("Deleting comment " + commentId + " requested by " + requesterId);

        String commentOwnerId = commentService.getCommentOwner(commentId);

        if (!commentOwnerId.equals(requesterId) && !role.equals("admin")) {
            logger.warn("User " + requesterId + " attempted to delete comment " + commentId + " that they do not own");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized to delete this comment.");
        }

        commentService.deleteComment(commentId);

        return ResponseEntity.status(HttpStatus.OK).body("Comment was successfully deleted.");
    }

    @DeleteMapping("/id/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable String postId,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        logger.trace("Deleting post " + postId + " requested by " + requesterId);

        String postOwnerId = postService.getPostOwner(postId);

        if (!postOwnerId.equals(requesterId)) {
            logger.warn("User " + requesterId + " attempted to delete post " + postId + " that they do not own");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized to delete this post.");
        }

        postService.deletePost(postId);

        return ResponseEntity.status(HttpStatus.OK).body("Post was successfully deleted.");
    }

    @PutMapping("/id/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable String postId, MultipartHttpServletRequest req,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        logger.trace("Updating post " + postId + " requested by " + requesterId);

        String postOwnerId = postService.getPostOwner(postId);

        if (!postOwnerId.equals(requesterId)) {
            logger.warn("User " + requesterId + " attempted to update post " + postId + " that they do not own");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized to update this post.");
        }

        MultipartFile file = req.getFile("file");
        String fileUrl = s3StorageService.uploadFile(file);

        postService.updatePost(postId, req, fileUrl);

        return ResponseEntity.status(HttpStatus.OK).body("Post was successfully updated.");
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deletePostsByUser(@PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        String role = tokenService.extractRole(token);

        if (!userId.equals(requesterId) && !role.equals("admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized to delete these posts.");
        }

        postService.deletePostsByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body("Posts were successfully deleted.");
    }

    @GetMapping("/trending/{fromDate}")
    public ResponseEntity<List<PostResponse>> getTrending(
            @PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getTrending(fromDate, requesterId));
    }

}
