package com.revature.Flumblr.controllers;

import com.revature.Flumblr.dtos.requests.NewProfileRequest;
import com.revature.Flumblr.dtos.responses.ProfileResponse;
import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.services.ProfileService;
import com.revature.Flumblr.services.S3StorageService;
import com.revature.Flumblr.services.TokenService;
import com.revature.Flumblr.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    ProfileService profileService;
    TokenService tokenService;
    S3StorageService s3StorageService;
    UserService userService;

    @GetMapping("/{id}")
    ResponseEntity<ProfileResponse> readProfileBio(@PathVariable String id,
                                                   @RequestHeader("Authorization") String token) {

        //handle invalid token
        tokenService.validateToken(token, id);
        Profile prof = profileService.getProfileByUserId(id);
        // frontend needs username for profile display
        User username = userService.findById(id);
        // include profile id, image url, bio, and themeName in response body for frontend
        ProfileResponse res = new ProfileResponse(username.getUsername(), prof.getId(), prof.getProfile_img(), prof.getBio(), prof.getTheme().getName());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // upload profile image
    @PatchMapping("/upload/{id}")
    ResponseEntity<?> updateProfileImage(@RequestPart("file") MultipartFile file,
                                         @PathVariable String id,
                                         @RequestParam("id") String profileId,
                                         //@RequestPart("profileId") NewProfileRequest profileId,
                                         @RequestHeader("Authorization") String token) {

        //handle invalid token
        tokenService.validateToken(token, id);
        String fileURL = null;
        if(file != null) {
            //need to get/delete old profile image as new one is uploaded
            fileURL = s3StorageService.uploadFile(file);
        }
        profileService.setProfileImg(profileId, fileURL);
        //profileService.setProfileImg(profileId.getProfileId(), fileURL);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // update profile bio
    @PatchMapping("/bio/{id}")
    ResponseEntity<?> updateProfileBio(@RequestBody NewProfileRequest req,
                                       @PathVariable String id,
                                       @RequestHeader("Authorization") String token) {

        //handle invalid token
        tokenService.validateToken(token, id);
        profileService.setBio(req.getProfileId(), req.getBio());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // update theme
    @PatchMapping("/theme/{id}")
    ResponseEntity<?> updateTheme(@PathVariable String id,
                                  @RequestBody NewProfileRequest req,
                                  @RequestHeader("Authorization") String token) {

        //handle invalid token
        tokenService.validateToken(token, id);
        profileService.setTheme(req.getProfileId(), req.getThemeName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // endpoint for up to 5 tags/interests to be stored
    // guessing these will be retrieved and stored when viewing posts to prioritize them
    // so a post and get mapping for tags? and delete

}
