package com.revature.Flumblr.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Principal class represents the authenticated user's principal
 * information.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Principal {
    private String id;
    private String username;
    private String token;

    public Principal(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}