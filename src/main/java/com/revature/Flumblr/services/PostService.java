package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_exceptions.FileNotUploadedException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.ArrayList;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.services.codecommit.model.FileContentRequiredException;
import com.revature.Flumblr.dtos.requests.NewPostRequest;
import com.revature.Flumblr.dtos.responses.PostResponse;
import com.revature.Flumblr.entities.User;

import lombok.AllArgsConstructor;

import com.revature.Flumblr.entities.Follow;
import com.revature.Flumblr.entities.Post;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<PostResponse> getFeed(String userId, int page) {
        User user = userService.findById(userId);
        List<User> following = new ArrayList<User>();
        for (Follow follow : user.getFollows()) {
            following.add(follow.getFollow());
        }
        List<Post> posts = postRepository.findAllByUserIn(following,
                PageRequest.of(page, 20, Sort.by("createTime").descending()));
        List<PostResponse> resPosts = new ArrayList<PostResponse>();
        for (Post userPost : posts) {
            resPosts.add(new PostResponse(userPost));
        }
        return resPosts;
    }

    public List<PostResponse> getUserPosts(String userId) {
        List<Post> userPosts = this.postRepository.findByUserIdOrderByCreateTimeDesc(userId);
        List<PostResponse> resPosts = new ArrayList<PostResponse>();
        for (Post userPost : userPosts) {
            resPosts.add(new PostResponse(userPost));
        }
        return resPosts;
    }

    public PostResponse getPost(String postId) {
        Optional<Post> userPost = this.postRepository.findById(postId);
        if (userPost.isEmpty())
            throw new ResourceNotFoundException("Post(" + postId + ") Not Found");
        return new PostResponse(userPost.get());
    }

    public Post findById(String postId) {
        Optional<Post> userPost = this.postRepository.findById(postId);
        if (userPost.isEmpty())
            throw new ResourceNotFoundException("Post(" + postId + ") Not Found");
        return userPost.get();
    }
    public String getPostOwner(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " was not found"));
        return post.getUser().getId();
        }

    public void deletePost(String postId) {
        try {
            postRepository.deleteById(postId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Post with id " + postId + " was not found");
        }
        }



    public void createPost(MultipartHttpServletRequest req, String fileUrl, String userId) {

        Optional<User> userOpt = userRepository.findById(userId);

        User user = userOpt.get();
        
        String message = req.getParameter("message");
        if(message == null){
            message = "no caption";
        }
        String mediaType = req.getParameter("mediaType");
        if(mediaType == null){
            throw new FileNotUploadedException("Media Type can not be empty!");
        }


        Post post = new Post(message, mediaType, fileUrl, user);
        
        postRepository.save(post);

    }   
    public PostResponse updatePost(String postId, MultipartHttpServletRequest req, String fileUrl) {
        Post post = this.findById(postId);
        String newMessage = req.getParameter("message");
        String newMediaType = req.getParameter("mediaType");

        if(newMessage != null && !newMessage.isEmpty()) {
            post.setMessage(newMessage);
        }

        if(newMediaType != null && !newMediaType.isEmpty()) {
            post.setMediaType(newMediaType);
        }

        if(fileUrl != null && !fileUrl.isEmpty()) {
            post.setS3Url(fileUrl);
        }
        post.setEditTime(new Date());
        postRepository.save(post);
        PostResponse response = new PostResponse(post);
        return response;
    }


}
