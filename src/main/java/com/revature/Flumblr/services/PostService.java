package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.User;

import lombok.AllArgsConstructor;

import com.revature.Flumblr.entities.Follow;
import com.revature.Flumblr.entities.Post;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public List<Post> getFeed(String userId, int page) {
        User user = userService.findById(userId);
        List<User> following = new ArrayList<User>();
        for (Follow follow : user.getFollows()) {
            following.add(follow.getFollow());
        }
        return postRepository.findAllByUserIn(following,
                PageRequest.of(page, 20, Sort.by("createTime").descending()));
    }

    public List<Post> getUserPosts(String userId) {
        return this.postRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public Post getPost(String postId) {
        Optional<Post> userPost = this.postRepository.findById(postId);
        if (userPost.isEmpty())
            throw new ResourceNotFoundException("Post(" + postId + ") Not Found");
        return userPost.get();
    }

    public Post findById(String postId) {
        Optional<Post> userPost = this.postRepository.findById(postId);
        if (userPost.isEmpty())
            throw new ResourceNotFoundException("Post(" + postId + ") Not Found");
        return userPost.get();
    }

}
