package com.revature.Flumblr.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.ProfileVote;
import com.revature.Flumblr.repositories.ProfileVoteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProfileVoteService {
    ProfileVoteRepository profileVoteRepo;

    public void vote(ProfileVote vote) {
        profileVoteRepo.save(vote);
    }

    public ProfileVote findById(String id) {
        return profileVoteRepo.findById(id).orElse(null);
    }

    public void delete(ProfileVote vote) {
        profileVoteRepo.delete(vote);
    }

    public ProfileVote findByUserAndProfile(User user, Profile profile) {
        return profileVoteRepo.findByUserAndProfile(user, profile).orElse(null);
    }
}