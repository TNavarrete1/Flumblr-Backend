package com.revature.Flumblr.services;

import com.revature.Flumblr.dtos.requests.NewCommentRequest;
import com.revature.Flumblr.entities.Comment;
import com.revature.Flumblr.repositories.CommentRepository;
import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.entities.Post;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostServices {
    private final CommentRepository commentRepository;
   private final UserRepository userRepository;
   private final PostRepository postRepository;
    public void commentOnPost(NewCommentRequest req) {
        User user = userRepository.getReferenceById(req.getUser_id());
        Post post = postRepository.getReferenceById(req.getPost_id());
        Comment com = new Comment(req.getComment(), post, user);
        commentRepository.save(com);     
    }   
}