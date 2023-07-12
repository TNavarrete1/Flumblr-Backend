package com.revature.Flumblr.utils.custom_classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortedTag {
    private String name;
    private double score;

    public SortedTag(String name, double score) {
        this.name = name;
        this.score = score;
    }
}
