package com.revature.Flumblr.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewCommentRequest {
    private String comment;
    private String postId;
    private String userId;
    private String gifUrl;
    private String[] mentions;
}
