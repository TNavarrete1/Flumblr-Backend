package com.revature.Flumblr.controllers;

import com.revature.Flumblr.dtos.requests.NewProfileRequest;
import com.revature.Flumblr.services.ProfileService;
import com.revature.Flumblr.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    ProfileService profileService;
    TokenService tokenService;

    //post profile -- can probably just create blank profile at registration and update img/bio from profile page when desired
//    @PostMapping("/create")
//    ResponseEntity<?> postProfile(@RequestBody NewProfileRequest req, @RequestHeader("Authorization") String token) {
//        tokenService.validateToken(token, req.getUserId());
//        //probably will need some type of conversion if not done at frontend to make the image a byte[]
//        profileService.save(req);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    //update profile
    @PatchMapping("/upload")
    ResponseEntity<?> updateProfileImage(@RequestParam("imgFile") MultipartFile file,
                                         @RequestBody NewProfileRequest req,
                                         @RequestHeader("Authorization") String token) throws IOException {

        tokenService.validateToken(token, req.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(profileService.setProfileImg(file.getBytes(), req.getUserId()));
    }

    @PatchMapping("/bio")
    ResponseEntity<?> updateProfileBio(@RequestBody NewProfileRequest req, @RequestHeader("Authorization") String token) {
        tokenService.validateToken(token, req.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(profileService.setBio(req));
    }

}
