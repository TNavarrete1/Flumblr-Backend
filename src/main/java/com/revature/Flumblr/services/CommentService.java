package com.revature.Flumblr.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.dtos.requests.NewCommentRequest;
import com.revature.Flumblr.entities.Comment;
import com.revature.Flumblr.entities.CommentMention;
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.CommentRepository;
import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;
    private final NotificationTypeService notificationTypeService;

    public String commentOnPost(NewCommentRequest req) {
        User user = userRepository.getReferenceById(req.getUserId());
        Post post = postRepository.getReferenceById(req.getPostId());
        Comment com = new Comment(req.getComment(), post, user, req.getGifUrl());

        Set<CommentMention> mentionsSet = new HashSet<CommentMention>();

        for(String mentionName : req.getMentions()) {
            Optional<User> optMentioned = userRepository.findByUsername(mentionName);
            if(optMentioned.isPresent()) {
                User mentioned = optMentioned.get();
                notificationService.createNotification(user.getUsername() +
                    " mentioned you in a comment", "comment:" + com.getId(), mentioned, 
                    notificationTypeService.findByName("commentMention"));
                mentionsSet.add(new CommentMention(mentioned, com));
            }
        }
        com.setCommentMentions(mentionsSet);

        commentRepository.save(com);

        notificationService.createNotification(user.getUsername() + " commented on your post",
            "post:" + post.getId(), post.getUser(), notificationTypeService.findByName("postComment"));

         return  com.getId();
    }

    public Comment findById(String commentId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Comment(" + commentId + ") Not Found");
        }
        return commentOpt.get();
    }

    public String getCommentOwner(String commentId) {
        Comment comment = findById(commentId);
        return comment.getUser().getId();
    }

    public void deleteComment(String commentId) {
        Comment comment = findById(commentId);
        commentRepository.delete(comment);
    }

}
