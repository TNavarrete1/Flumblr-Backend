package com.revature.Flumblr.controllers;

import com.revature.Flumblr.dtos.requests.DeleteImageRequest;
import com.revature.Flumblr.dtos.requests.NewInterestRequest;
import com.revature.Flumblr.dtos.requests.NewProfileRequest;
import com.revature.Flumblr.dtos.responses.ProfileResponse;
import com.revature.Flumblr.dtos.responses.TagInterestResponse;
import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.Tag;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.services.*;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;
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

    private final ProfileService profileService;
    private final TokenService tokenService;
    private final S3StorageService s3StorageService;
    private final UserService userService;
    private final TagService tagService;

    @GetMapping("/{id}")
    ResponseEntity<ProfileResponse> readProfileBio(@PathVariable String id) {

        Profile prof = profileService.getProfileByUserId(id);
        // frontend needs username for profile display
        User username = userService.findById(id);
        // and profile votes
        int votes = profileService.getTotal(prof.getId());
        // include profile id, image url, bio, and themeName in response body for frontend
        ProfileResponse res = new ProfileResponse(username.getUsername(), prof.getId(), prof.getProfile_img(), prof.getBio(), prof.getTheme().getName(), votes);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // upload profile image
    @PatchMapping("/upload/{id}")
    ResponseEntity<?> updateProfileImage(@RequestPart("file") MultipartFile file,
            @PathVariable String id,
            // @RequestParam("id") String profileId,
            @RequestPart("profileId") NewProfileRequest profileId,
            @RequestHeader("Authorization") String token) {

        // handle invalid token
        tokenService.validateToken(token, id);
        String fileURL = null;
        if (file != null) {
            // need to get/delete old profile image as new one is uploaded
            fileURL = s3StorageService.uploadFile(file);
        }
        // profileService.setProfileImg(profileId, fileURL);
        profileService.setProfileImg(profileId.getProfileId(), fileURL);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // update profile bio
    @PatchMapping("/bio/{id}")
    ResponseEntity<?> updateProfileBio(@RequestBody NewProfileRequest req,
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {

        // handle invalid token
        tokenService.validateToken(token, id);
        profileService.setBio(req.getProfileId(), req.getBio());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // update theme
    @PatchMapping("/theme/{id}")
    ResponseEntity<?> updateTheme(@PathVariable String id,
            @RequestBody NewProfileRequest req,
            @RequestHeader("Authorization") String token) {

        // handle invalid token
        tokenService.validateToken(token, id);
        profileService.setTheme(req.getProfileId(), req.getThemeName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/tags/{profileId}")
    ResponseEntity<TagInterestResponse> getProfileInterests(@RequestHeader("Authorization") String token,
                                                            @PathVariable String profileId) {

        // handle invalid token
        tokenService.extractUserId(token);
        TagInterestResponse res = new TagInterestResponse(profileService.getTagsByProfile(profileId));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/tags")
    ResponseEntity<?> postProfileInterests(@RequestHeader("Authorization") String token,
            @RequestBody NewInterestRequest req) {

        // handle invalid token
        tokenService.validateToken(token, req.getUser_id());
        profileService.assignTagToProfile(req.getProfile_id(), req.getTag_name());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/tags")
    public ResponseEntity<?> deleteTagAssociatedWithProfile(@RequestHeader("Authorization") String token,
            @RequestBody NewInterestRequest req) {

        // handle invalid token
        tokenService.validateToken(token, req.getUser_id());
        Tag foundTag = tagService.findByName(req.getTag_name());
        profileService.deleteTagsFromProfile(req.getProfile_id(), foundTag);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deleteImage(@RequestHeader("Authorization") String token,
                                         @RequestBody DeleteImageRequest req) {

        //handle invalid token
        tokenService.extractUserId(token);
        String defaultImgURL = "https://flumblr.s3.amazonaws.com/879fbd85-d8c1-43c6-a31a-de78c04b3918-profile.jpg";
        if(req.getUrl().equals(defaultImgURL)) {
            throw new BadRequestException("Cannot delete the default image from profile");
        }
        profileService.setProfileImg(req.getProfileId(), defaultImgURL);
        s3StorageService.deleteFileFromS3Bucket(req.getUrl());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
