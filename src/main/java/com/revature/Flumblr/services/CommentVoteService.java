package com.revature.Flumblr.services;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.dtos.requests.CommentVoteRequest;
import com.revature.Flumblr.entities.Comment;
import com.revature.Flumblr.entities.CommentVote;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.CommentVoteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CommentVoteService {
    CommentVoteRepository commentVoteRepo;
    CommentService commentService;
    UserService userService;

    public void vote(CommentVoteRequest req) {
        User user = userService.getById(req.getUserId());
        Comment comment = commentService.findById(req.getCommentId());
        CommentVote vote = findByUserAndComment(user, comment);
        if (vote == null) {
            vote = new CommentVote(user, comment, req.isVote());
        } else {
            if (vote.isVote() == req.isVote()) {
                delete(vote);
                return;
            } else {
                vote.setVote(req.isVote());
            }
        }
        save(vote);
    }

    public void save(CommentVote vote) {
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
