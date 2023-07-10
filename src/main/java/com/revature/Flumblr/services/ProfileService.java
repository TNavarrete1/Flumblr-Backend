package com.revature.Flumblr.services;

import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.Theme;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.ProfileRepository;
import com.revature.Flumblr.repositories.ThemeRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;
import com.revature.Flumblr.utils.custom_exceptions.FileNotUploadedException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ProfileService {

    UserRepository userRepository;
    ProfileRepository profileRepository;
    ThemeRepository themeRepository;

    public Profile getProfileByUserId(String id) {
        User existingUser = userRepository.getReferenceById(id);
        return profileRepository.getProfileByUser(existingUser);
    }

    public void setProfileImg(String profileId, String URL) {
        if(URL == null) {
            throw new FileNotUploadedException("An error occurred uploading the profile image.");
        }
        profileRepository.setProfileImg(profileId, URL);
    }

    public void setBio(String profileId, String bio) {
        if(bio == null) {
            throw new BadRequestException("Cannot submit a null bio.");
        }
        profileRepository.setBio(profileId, bio);
    }

    public void setTheme(String profileId, String themeName) {
        Theme theme = themeRepository.findByName(themeName);
        if(theme == null) {
            throw new ResourceNotFoundException("No theme with name: " + themeName + "found.");
        }
        profileRepository.setTheme(profileId, theme);
    }

    public Profile findById(String id) {
        Optional<Profile> opt = profileRepository.findById(id);
        if(opt.isEmpty()) {
            throw new ResourceNotFoundException("Unable to find profile ID: " + id);
        }
        return opt.get();
    }

}
