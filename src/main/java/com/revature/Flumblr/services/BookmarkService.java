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
        return bookmarksRepository.save(new Bookmark(user, post));
    }

    public void removeBookmark(DeleteBookmarkRequest request) {
        User user = userService.findById(request.getUserId());
        Post post = postService.findById(request.getPostId());
        bookmarksRepository.delete(new Bookmark(request.getBookmarkId(), user,
                post));
    }
}
