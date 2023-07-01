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
    private final CommentRepository repo;
   private final UserRepository userRepo;
   private final PostRepository postRepo;
    public void commentOnPost(NewCommentRequest req) {
        User user = userRepo.getReferenceById(req.getUser_id());
        Post post = postRepo.getReferenceById(req.getPost_id());
        Comment com = new Comment(req.getComment(), post, user);
        repo.save(com);     
    }   
}