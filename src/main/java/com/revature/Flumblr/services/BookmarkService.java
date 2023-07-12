package com.revature.Flumblr.services;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.dtos.requests.BookmarkRequest;
import com.revature.Flumblr.dtos.requests.DeleteBookmarkRequest;
import com.revature.Flumblr.entities.Bookmark;
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.BookmarksRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookmarkService {
    private final PostService postService;
    private final UserService userService;
    private final BookmarksRepository bookmarksRepository;

    public Bookmark bookmarkPost(BookmarkRequest request) {
        User user = userService.findById(request.getUserId());
        Post post = postService.findById(request.getPostId());
        Bookmark bookmark = findByUserAndPost(user, post);
        if (bookmark == null) {
            return bookmarksRepository.save(new Bookmark(user, post));
        }
        return bookmark;
    }

    public void removeBookmark(DeleteBookmarkRequest request) {
        User user = userService.findById(request.getUserId());
        Post post = postService.findById(request.getPostId());
        Bookmark bookmark = findByUserAndPost(user, post);
        if (bookmark != null) {
            bookmarksRepository.delete(bookmark);
        }

    }

    public Bookmark findByUserAndPost(User user, Post post) {
        return bookmarksRepository.findByUserAndPost(user, post).orElse(null);
    }
}
