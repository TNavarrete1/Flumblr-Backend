package com.revature.Flumblr.dtos.responses;

import com.revature.Flumblr.entities.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private String id;
    private String username;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

}
