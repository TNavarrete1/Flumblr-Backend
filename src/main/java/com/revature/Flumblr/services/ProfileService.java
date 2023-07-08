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

    UserRepository userRepo;
    ProfileRepository profileRepo;

    public Profile getProfileByUser(User user) {
        return profileRepo.getProfileByUser(user);
    }

    public Profile setProfileImg(String img, NewProfileRequest req) {
        User existingUser = userRepo.getReferenceById(req.getUserId());
        return profileRepo.setProfileImg(img, existingUser);
    }

    public Profile setBio(NewProfileRequest req) {
        User existingUser = userRepo.getReferenceById(req.getUserId());
        return profileRepo.setBio(req.getBio(), existingUser);
    }

}
