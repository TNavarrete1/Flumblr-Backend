package com.revature.Flumblr.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProfileResponse {

    private String profileId;
    private String profile_img;
    private String bio;
    private String themeName;

}
