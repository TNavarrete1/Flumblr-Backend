package com.revature.Flumblr.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.repositories.ProfileRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Profile findById(String profileId) {
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        if (profileOpt.isEmpty())
            throw new ResourceNotFoundException("couldn't find profile for id " + profileId);
        return profileOpt.get();
    }

}
