package com.revature.Flumblr.services;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.PostVote;
import com.revature.Flumblr.repositories.PostVoteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostVoteService {
    PostVoteRepository postVoteRepo;

    public void vote(PostVote vote) {
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
