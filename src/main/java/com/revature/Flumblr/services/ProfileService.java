package com.revature.Flumblr.services;
import com.revature.Flumblr.dtos.requests.NewProfileRequest;
import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.ProfileRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_exceptions.ProfileNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ProfileService {

    UserRepository userRepo;
    ProfileRepository profileRepo;

    public Profile findById(String profileId) {
        Optional<Profile> profileOpt = profileRepo.findById(profileId);
        if (profileOpt.isEmpty()) {
            throw new ProfileNotFoundException("No profile located for specified Id.");
        }
        return profileOpt.get();
    }

    public Profile save(NewProfileRequest req) {
        User existingUser = userRepo.getReferenceById(req.getUserId());
        Profile newProfile = new Profile(existingUser, req.getProfile_img(), req.getBio());
        return profileRepo.save(newProfile);
    }

    public Profile update(NewProfileRequest req) {
        User existingUser = userRepo.getReferenceById(req.getUserId());
        Profile prof = new Profile(existingUser, req.getProfile_img(), req.getBio());
        return profileRepo.update(prof);
    }

}
