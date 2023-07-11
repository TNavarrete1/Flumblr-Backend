package com.revature.Flumblr.utils.custom_classes;

import com.revature.Flumblr.dtos.responses.PostResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortedPost {
    private PostResponse content;
    private double score;

    public SortedPost(PostResponse content) {
        this.content = content;
    }
}
