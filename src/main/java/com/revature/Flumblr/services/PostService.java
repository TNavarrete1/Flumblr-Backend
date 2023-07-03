package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.CommentRepository;
import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_exceptions.PostNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
//import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.revature.Flumblr.dtos.requests.NewCommentRequest;
import com.revature.Flumblr.dtos.responses.PostResponse;
import com.revature.Flumblr.entities.User;

import lombok.AllArgsConstructor;

import com.revature.Flumblr.entities.Comment;
import com.revature.Flumblr.entities.Follow;
import com.revature.Flumblr.entities.Post;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserService userService;
       private final CommentRepository commentRepository;
   private final UserRepository userRepository;
   private final PostRepository postRepository;

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

        public void commentOnPost(NewCommentRequest req) {
        User user = userRepository.getReferenceById(req.getUser_id());
        Post post = postRepository.getReferenceById(req.getPost_id());
        Comment com = new Comment(req.getComment(), post, user);
        commentRepository.save(com);     
    }   
}
