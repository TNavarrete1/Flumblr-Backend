package com.revature.Flumblr.controllers;

import com.revature.Flumblr.dtos.requests.NewProfileRequest;
import com.revature.Flumblr.services.ProfileService;
import com.revature.Flumblr.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    ProfileService profileService;
    TokenService tokenService;

    //post profile
    @PostMapping("/create")
    ResponseEntity<?> postProfile(@RequestBody NewProfileRequest req, @RequestHeader("Authorization") String token) {
        tokenService.validateToken(token, req.getUserId());
        //probably will need some type of conversion if not done at frontend to make the image a byte[]
        profileService.save(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //update profile
    @PutMapping("/update")
    ResponseEntity<?> updateProfile(@RequestBody NewProfileRequest req, @RequestHeader("Authorization") String token) {
        tokenService.validateToken(token, req.getUserId());
        //considerations to be made about how to handle possibly incomplete information
        //also might need conversion of image to byte[] if not done at frontend
        profileService.update(req);
        return ResponseEntity.status(HttpStatus.OK).body(req);
    }

}
