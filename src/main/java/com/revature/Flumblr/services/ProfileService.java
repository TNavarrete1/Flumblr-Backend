package com.revature.Flumblr.services;

import com.revature.Flumblr.dtos.requests.NewProfileRequest;
import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.ProfileRepository;
import com.revature.Flumblr.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProfileService {

    UserRepository userRepository;
    ProfileRepository profileRepository;

    public Profile getProfileByUserId(String id) {
        User existingUser = userRepository.getReferenceById(id);
        return profileRepository.getProfileByUser(existingUser);
    }

    public Profile setProfileImg(String profileId, String URL) {
        return profileRepository.setProfileImg(profileId, URL);
    }

    public Profile setBio(String profileId, String bio) {
        return profileRepository.setBio(profileId, bio);
    }

}
