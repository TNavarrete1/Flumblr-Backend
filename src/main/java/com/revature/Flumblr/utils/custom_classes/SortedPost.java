package com.revature.Flumblr.utils.custom_classes;

import com.revature.Flumblr.entities.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortedPost {
    private Post content;
    private double score;
    private int upvotes;
    private int downvotes;
    public SortedPost(Post content) {
        this.content = content;
    }
}
