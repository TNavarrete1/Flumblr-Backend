package com.revature.Flumblr.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewPostRequest {
    private String id;

    private String username;

    private String message;

    private String mediaType;

    private String[] tags = new String[] {};

    private String[] mentions = new String[] {};
}
