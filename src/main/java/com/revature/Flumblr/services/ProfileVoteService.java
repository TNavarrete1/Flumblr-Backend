package com.revature.Flumblr.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.dtos.requests.ProfileVoteRequest;
import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.ProfileVote;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.ProfileVoteRepository;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@Service
public class ProfileVoteService {
    ProfileVoteRepository profileVoteRepo;
    // ProfileService profileService;
    UserService userService;

    public void vote(ProfileVoteRequest req) {

        // Patrick will handle profileService

        // User user = userService.findById(req.getUserId());
        // Profile profile = profileService.findById(req.getProfileId());

        User user = null;
        Profile profile = null;

        ProfileVote vote = findByUserAndProfile(user, profile);
        if (vote == null) {
            vote = new ProfileVote(user, profile, req.isVote());
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

    public void save(ProfileVote vote) {
        profileVoteRepo.save(vote);
    }

    public ProfileVote findById(String id) {
        return profileVoteRepo.findById(id).orElse(null);
    }

    public void delete(ProfileVote vote) {
        profileVoteRepo.delete(vote);
    }

    public ProfileVote findByUserAndProfile(User user, Profile profile) {
        // return profileVoteRepo.findByUserAndProfile(user, profile).orElse(null);
        return null;
    }
}