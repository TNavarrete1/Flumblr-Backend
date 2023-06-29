package com.revature.Flumblr.services;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.dtos.requests.PostVoteRequest;
import com.revature.Flumblr.entities.PostVote;
import com.revature.Flumblr.repositories.PostVoteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostVoteService {
    PostVoteRepository postVoteRepo;
    PostService postService;
    UserService userService;

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
    }

    public void save(PostVote vote) {
        postVoteRepo.save(vote);
    }

    public PostVote findById(String id) {
        return postVoteRepo.findById(id).orElse(null);
    }

    public void delete(PostVote vote) {
        postVoteRepo.delete(vote);
    }

    public PostVote findByUserAndPost(User user, Post post) {
        return postVoteRepo.findByUserAndPost(user, post).orElse(null);
    }
}
