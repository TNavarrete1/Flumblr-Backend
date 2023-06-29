package com.revature.Flumblr.services;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.CommentVote;
import com.revature.Flumblr.repositories.CommentVoteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CommentVoteService {
    CommentVoteRepository commentVoteRepo;

    public void vote(CommentVote vote) {
        commentVoteRepo.save(vote);
    }

    public CommentVote findById(String id) {
        return commentVoteRepo.findById(id).orElse(null);
    }

    public void delete(CommentVote vote) {
        commentVoteRepo.delete(vote);
    }

    public CommentVote findByUserAndComment(User user, Comment comment) {

        return commentVoteRepo.findByUserAndComment(user, comment).orElse(null);
    }
}
