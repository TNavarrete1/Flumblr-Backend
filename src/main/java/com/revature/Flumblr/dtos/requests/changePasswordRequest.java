package com.revature.Flumblr.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class changePasswordRequest {


    private String password;
    private String confirmPassword;
    private String token;
    
}
