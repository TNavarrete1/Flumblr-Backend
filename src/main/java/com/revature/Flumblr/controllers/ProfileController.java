package com.revature.Flumblr.controllers;

import com.revature.Flumblr.dtos.requests.NewProfileRequest;
import com.revature.Flumblr.dtos.responses.ProfileResponse;
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

    //get profile bio (and image?) by user id -- need to figure out how frontend needs to receive the image
    //typical way to send binary in json is to base64 encode it.
    //java provides different ways to base64 encode and decode a byte[] - one way is DatatypeConverter
    //String base64Encoded = DatatypeConverter.printBase64Binary(originalBytes);
    //byte[] base64Decoded = DatatypeConverter.parseBase64Binary(base64Encoded);
    @GetMapping("/{id}")
    ResponseEntity<ProfileResponse> readProfileBio(@PathVariable String id,
                                                   @RequestHeader("Authorization") String token) {
        tokenService.validateToken(token, id);
        ProfileResponse res = new ProfileResponse(profileService.getProfileByUser(id).getBio());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //upload profile image
    @PatchMapping("/upload")
    ResponseEntity<?> updateProfileImage(@RequestParam("imgFile") MultipartFile file,
                                         @RequestBody NewProfileRequest req,
                                         @RequestHeader("Authorization") String token) throws IOException {

        tokenService.validateToken(token, req.getUserId());
        //return ResponseEntity.status(HttpStatus.OK).body(profileService.setProfileImg(file.getBytes(), req));
        profileService.setProfileImg(file.getBytes(), req);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //update profile bio
    @PatchMapping("/bio")
    ResponseEntity<?> updateProfileBio(@RequestBody NewProfileRequest req, @RequestHeader("Authorization") String token) {
        tokenService.validateToken(token, req.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(profileService.setBio(req));
    }

}
