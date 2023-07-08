package com.revature.Flumblr.services;

import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.ProfileRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Profile setTheme(String profileId, String themeName) {
        return profileRepository.setTheme(profileId, themeName);
    }

    public Profile findById(String id) {
        Optional<Profile> opt = profileRepository.findById(id);
        if(opt.isEmpty()) {
            throw new ResourceNotFoundException("Unable to find profile ID: " + id);
        }
        return opt.get();
    }

}
