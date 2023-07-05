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

//    public Profile findById(String profileId) {
//        Profile aProfile = new Profile(profileId);
//        return profileRepo.findByProfileId(aProfile);
//    }

    public Profile getProfileByUser(String id) {
        User existingUser = userRepo.getReferenceById(id);
        return profileRepo.getProfileByUser(existingUser);
    }

    public void setProfileImg(String img, NewProfileRequest req) {
        User existingUser = userRepo.getReferenceById(req.getUserId());
        profileRepo.setProfileImg(img, existingUser);
    }

    public Profile setBio(NewProfileRequest req) {
        User existingUser = userRepo.getReferenceById(req.getUserId());
        return profileRepo.setBio(req.getBio(), existingUser);
    }

    public Profile getProfileByUser(User user) {
        return profileRepo.getProfileByUser(user);
    }

}
