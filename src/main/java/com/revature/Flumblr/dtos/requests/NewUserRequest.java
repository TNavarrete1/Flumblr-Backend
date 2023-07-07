package com.revature.Flumblr.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The NewUserRequest class represents a request for creating a new user.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewUserRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
