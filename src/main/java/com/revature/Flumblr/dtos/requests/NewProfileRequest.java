package com.revature.Flumblr.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewProfileRequest {

    private String userId;
    //type of profile_img is subject to change - considering Blob
    private String profile_img;
    private String bio;

}
