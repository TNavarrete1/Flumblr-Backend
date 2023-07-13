package com.revature.Flumblr.services;

import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.repositories.*;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;
import com.revature.Flumblr.utils.custom_exceptions.FileNotUploadedException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ThemeRepository themeRepository;
    private final TagRepository tagRepository;
    private final ProfileVoteRepository profileVoteRepository;
    private final S3StorageService s3StorageService;

    public Profile getProfileByUserId(String id) {
        User existingUser = userRepository.getReferenceById(id);
        return profileRepository.getProfileByUser(existingUser);
    }

    public void deleteAndSetNewProfileImg(String profileId, String URL) {
        if(URL==null) {
            throw new FileNotUploadedException("An error occurred uploading the profile image.");
        }
        String oldImageUrl = profileRepository.getOldProfileUrl(profileId);
        String defaultImgURL = "https://flumblr.s3.amazonaws.com/f3c5b50f-8683-4502-8954-494c0fca1487-profile.jpg";
        profileRepository.setProfileImg(profileId, URL);
        if(oldImageUrl.equals(defaultImgURL)) {
            return;
        } else {
            s3StorageService.deleteFileFromS3Bucket(oldImageUrl);
        }
    }

    public void setProfileImg(String profileId, String URL) {
        if (URL == null) {
            throw new FileNotUploadedException("An error occurred uploading the profile image.");
        }
        profileRepository.setProfileImg(profileId, URL);
    }

    public void setBio(String profileId, String bio) {
        if(bio == null || bio.length() > 254) {
            throw new BadRequestException("A valid Bio can be up to 254 characters in length.");
        }
        profileRepository.setBio(profileId, bio);
    }

    public void setTheme(String profileId, String themeName) {
        Optional<Theme> themeOpt = themeRepository.findByName(themeName);
        if (themeOpt.isEmpty()) {
            throw new ResourceNotFoundException("No theme with name: " + themeName + " found.");
        }
        profileRepository.setTheme(profileId, themeOpt.get());
    }

    public Profile findById(String id) {
        Optional<Profile> opt = profileRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException("Unable to find profile ID: " + id);
        }
        return opt.get();
    }

    public Profile assignTagToProfile(String profileId, String tagName) {
        Set<Tag> tagSet = null;
        Tag tag = null;
        Profile profile = profileRepository.findById(profileId).get();
        tagSet = profile.getTags();
        if (tagSet.size() >= 5) {
            throw new BadRequestException("A profile cannot store more than five (5) tags at a time.");
        }
        Optional<Tag> tagOpt = tagRepository.findByName(tagName);
        if(tagOpt.isEmpty()) {
            tag = tagRepository.save(new Tag(tagName));
        } else {
            tag= tagOpt.get();
        }
        tagSet.add(tag);
        profile.setTags(tagSet);
        return profileRepository.save(profile);
    }

    public Set<Tag> getTagsByProfile(String profileId) {
        return profileRepository.findById(profileId).get().getTags();
    }

   public void deleteTagsFromProfile(String profileId, Tag tag) {
        Profile profile = profileRepository.findById(profileId).get();
        profile.getTags().remove(tag);
        profileRepository.save(profile);
   }

   public int getTotal(String profileId) {
        int votes = 0;
        Optional<Profile> profOpt = profileRepository.findById(profileId);
        if(profOpt.isEmpty()) {
            throw new ResourceNotFoundException("Profile not found with id: " + profileId);
        }
        List<ProfileVote> list = profileVoteRepository.findAllByProfile(profOpt.get());
        if(list.size() == 0 ) {
            return 0;
        }
        for(ProfileVote vote : list) {
            if(vote.isVote()) votes++;
        }
        return votes;
   }

}
