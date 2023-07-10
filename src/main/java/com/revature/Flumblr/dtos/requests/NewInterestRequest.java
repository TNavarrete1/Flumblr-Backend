package com.revature.Flumblr.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewInterestRequest {

    private String user_id;
    //possibly change to a list of tags?
    private String tag_name;

}
