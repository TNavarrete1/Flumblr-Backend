package com.revature.Flumblr.controllers;

import com.revature.Flumblr.dtos.requests.NewProfileRequest;
import com.revature.Flumblr.dtos.responses.ProfileResponse;
import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.Theme;
import com.revature.Flumblr.services.ProfileService;
import com.revature.Flumblr.services.S3StorageService;
import com.revature.Flumblr.services.ThemeService;
import com.revature.Flumblr.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    ProfileService profileService;
    TokenService tokenService;
    S3StorageService s3StorageService;
    ThemeService themeService;

    @GetMapping("/{id}")
    ResponseEntity<ProfileResponse> readProfileBio(@PathVariable String id,
                                                   @RequestHeader("Authorization") String token) {

        //handle invalid token
        tokenService.validateToken(token, id);
        Profile prof = profileService.getProfileByUserId(id);
        Theme profTheme = themeService.getTheme(prof);
        // include profile id, image url, bio, and themeName in response body for frontend
        ProfileResponse res = new ProfileResponse(prof.getId(), prof.getProfile_img(), prof.getBio(), profTheme.getName());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // upload profile image
    @PatchMapping("/upload/{id}")
    ResponseEntity<?> updateProfileImage(MultipartHttpServletRequest req,
                                         @PathVariable String id,
                                         @RequestBody NewProfileRequest profileId,
                                         @RequestHeader("Authorization") String token) {

        //handle invalid token
        tokenService.validateToken(token, id);
        MultipartFile file = req.getFile("file");
        String fileURL = null;
        if(file != null) {
            //need to get/delete old profile image as new one is uploaded
            fileURL = s3StorageService.uploadFile(file);
        }
        profileService.setProfileImg(profileId.getProfileId(), fileURL);
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

    //can make theme endpoint here or add a theme controller if we're having a frontend admin create themes

}
