package com.revature.Flumblr.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.Flumblr.services.TokenService;
import com.revature.Flumblr.services.UserService;
import com.revature.Flumblr.services.VerificationService;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;
import com.revature.Flumblr.dtos.requests.NewLoginRequest;
import com.revature.Flumblr.dtos.requests.NewUserRequest;
import com.revature.Flumblr.dtos.requests.ResetRequest;
import com.revature.Flumblr.dtos.requests.changePasswordRequest;
import com.revature.Flumblr.dtos.responses.Principal;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.entities.Verification;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.repositories.VerifivationRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final VerifivationRepository verifivationRepository;
    private final VerificationService verificationService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody NewUserRequest req) {
        // // if username is not valid, throw exception
        if (!userService.isValidUsername(req.getUsername())) {
            throw new ResourceConflictException(
                    "Username needs to be 8-20 characters long and can only contain letters, numbers, periods, and underscores");
        }

        // if username is not unique, throw exception
        if (!userService.isUniqueUsername(req.getUsername())) {
            throw new ResourceConflictException("The username you selected has already been taken");
        }

        // if password is not valid, throw exception
        if (!userService.isValidPassword(req.getPassword())) {
            throw new ResourceConflictException(
                    "Password needs to be at least 8 characters long and contain at least one letter and one number");
        }

        // if password and confirm password do not match, throw exception
        if (!userService.isSamePassword(req.getPassword(), req.getConfirmPassword())) {
            throw new ResourceConflictException("Your password and confirm password do not match.");
        }
        // register user
        userService.registerUser(req);

        logger.info("Successfully Registered");

        // return 201 - CREATED
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/verify-account")
    public ResponseEntity<?> changePassword(@RequestParam("token") String token) {

        Verification verificationToken = verifivationRepository.findByVerificationToken(token);

        if (verificationToken != null) {
            User user = userRepository.findByEmailIgnoreCase(verificationToken.getUser().getEmail());

            user.setVerified(true);

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("Email is Verified successfully! ");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("The link is invalid or broken!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody NewLoginRequest req) {

        // userservice to call login method
        Principal principal = userService.login(req);

        logger.info("Successfully logged in");

        // create a jwt token
        String token = tokenService.generateJWT(principal);

        principal.setToken(token);
        // return status ok and return principal object
        return ResponseEntity.status(HttpStatus.OK).body(principal);
    }

    @PutMapping("/newpassword")
    public ResponseEntity<?> verify(@RequestBody changePasswordRequest req) {

        Verification verificationToken = verifivationRepository.findByVerificationToken(req.getToken());

        // if password is not valid, throw exception
        if (!userService.isValidPassword(req.getPassword())) {
            throw new ResourceConflictException(
                    "Password needs to be at least 8 characters long and contain at least one letter and one number");
        }

        // if password and confirm password do not match, throw exception
        if (!userService.isSamePassword(req.getPassword(), req.getConfirmPassword())) {
            throw new ResourceConflictException("Your password and confirm password do not match.");
        }

        if (verificationToken != null) {
            User user = userRepository.findByEmailIgnoreCase(verificationToken.getUser().getEmail());

            userService.changePassword(req, user);

            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body("The link is invalid or broken!");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetRequest req) {

        String email = req.getEmail();

        if (email.equals(null)) {
            throw new ResourceNotFoundException("Invalid email please provide a valid email.");
        }

        User user = userRepository.findByEmailIgnoreCase(email);

        if (user == null) {
            throw new ResourceNotFoundException("User can not be found by the provided email!");
        }

        logger.info("password reset requested from " + user.getUsername());

        // create a new verificationToken based on the found user and send the token
        // through email

        Verification verification = verifivationRepository.getByUserId(user.getId());

        if (verification == null) {
            throw new ResourceNotFoundException("Invalid or Broken link");
        }

        if(user.getVerified() == null || user.getVerified() == false) {

            SimpleMailMessage mailMessage = verificationService.composeVerification(email,
                    verification.getVerificationToken());

            verificationService.sendEmail(mailMessage);

        }

        else if (user.getVerified() == true) {

            SimpleMailMessage mailMessage = verificationService.composeResetPassword(email,
                    verification.getVerificationToken());

            verificationService.sendEmail(mailMessage);

        }

        return ResponseEntity.status(HttpStatus.OK).body("Instructions are sent to your Email successfully!");
    }

}
