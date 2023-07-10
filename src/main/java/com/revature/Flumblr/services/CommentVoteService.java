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
    private final CommentVoteRepository commentVoteRepository;
    private final CommentService commentService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final NotificationTypeService notificationTypeService;

    public void vote(CommentVoteRequest req) {
        User user = userService.findById(req.getUserId());
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
        notificationService.createNotification(user.getUsername() + " voted on your comment",
                "comment:" + comment.getId(), comment.getUser(), notificationTypeService.findByName("commentLike"));
    }

    public void save(CommentVote vote) {
        commentVoteRepository.save(vote);
    }

    public CommentVote findById(String id) {
        return commentVoteRepository.findById(id).orElse(null);
    }

    public void delete(CommentVote vote) {
        commentVoteRepository.delete(vote);
    }

    public CommentVote findByUserAndComment(User user, Comment comment) {

        return commentVoteRepository.findByUserAndComment(user, comment).orElse(null);
    }
}
