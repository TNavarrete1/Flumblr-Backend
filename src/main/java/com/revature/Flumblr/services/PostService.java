package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.utils.custom_exceptions.PostNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
//import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.revature.Flumblr.dtos.responses.PostResponse;
import com.revature.Flumblr.entities.User;

import lombok.AllArgsConstructor;

import com.revature.Flumblr.entities.Follow;
import com.revature.Flumblr.entities.Post;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserService userService;

    public List<PostResponse> getFeed(String userId, int page) {
        User user = userService.getUserById(userId);
        List<User> following = new ArrayList<User>();
        for(Follow follow : user.getFollows()) {
            following.add(follow.getFollow());
        }
        List<Post> posts =
            postRepo.findAllByUserIn(following, PageRequest.of(page, 20, Sort.by("createTime").descending()));
        List<PostResponse> resPosts = new ArrayList<PostResponse>();
        for(Post userPost : posts) {
            resPosts.add(new PostResponse(userPost));
        }
        return resPosts;
    }

    public List<PostResponse> getUserPosts(String userId) {
        List<Post> userPosts = this.postRepo.findByUserIdOrderByCreateTimeDesc(userId);
        List<PostResponse> resPosts = new ArrayList<PostResponse>();
        for(Post userPost : userPosts) {
            resPosts.add(new PostResponse(userPost));
        }
        return resPosts;
    }

    public PostResponse getPost(String postId) {
        Optional<Post> userPost = this.postRepo.findById(postId);
        if(userPost.isEmpty()) throw new PostNotFoundException(postId);
        return new PostResponse(userPost.get());
    }
}
