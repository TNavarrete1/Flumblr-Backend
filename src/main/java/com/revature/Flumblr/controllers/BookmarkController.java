package com.revature.Flumblr.controllers;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.Flumblr.dtos.requests.BookmarkRequest;
import com.revature.Flumblr.dtos.requests.DeleteBookmarkRequest;
import com.revature.Flumblr.services.BookmarkService;
import com.revature.Flumblr.services.TokenService;
import com.revature.Flumblr.services.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/bookmark")
@CrossOrigin
public class BookmarkController {
    private final BookmarkService userService;
    private final TokenService tokenService;

    @PostMapping("/addBookmark")
    public ResponseEntity<?> bookmarkPost(@RequestBody BookmarkRequest req,
            @RequestHeader("Authorization") String token) {

        tokenService.validateToken(token, req.getUserId());
        userService.bookmarkPost(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/removeBookmark")
    public ResponseEntity<?> removeBookmark(@RequestBody DeleteBookmarkRequest req,
            @RequestHeader("Authorization") String token) {

        tokenService.validateToken(token, req.getUserId());
        userService.removeBookmark(req);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
