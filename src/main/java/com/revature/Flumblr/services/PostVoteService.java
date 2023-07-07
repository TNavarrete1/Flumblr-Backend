package com.revature.Flumblr.services;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.dtos.requests.PostVoteRequest;
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.PostVote;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.PostVoteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostVoteService {
    private final PostVoteRepository postVoteRepository;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final NotificationTypeService notificationTypeService;

    public void vote(PostVoteRequest req) {
        User user = userService.findById(req.getUserId());
        Post post = postService.findById(req.getPostId());

        PostVote vote = findByUserAndPost(user, post);
        if (vote == null) {
            vote = new PostVote(user, post, req.isVote());
        } else {
            if (vote.isVote() == req.isVote()) {
                delete(vote);
                return;
            } else {
                vote.setVote(req.isVote());
            }
        }
        save(vote);
        notificationService.createNotification("User " + user.getUsername() + " voted on your post",
                "post:" + post.getId(), user, notificationTypeService.findByName("postLike"));
    }

    public void save(PostVote vote) {
        postVoteRepository.save(vote);
    }

    public PostVote findById(String id) {
        return postVoteRepository.findById(id).orElse(null);
    }

    public void delete(PostVote vote) {
        postVoteRepository.delete(vote);
    }

    public PostVote findByUserAndPost(User user, Post post) {
        return postVoteRepository.findByUserAndPost(user, post).orElse(null);
    }
}
