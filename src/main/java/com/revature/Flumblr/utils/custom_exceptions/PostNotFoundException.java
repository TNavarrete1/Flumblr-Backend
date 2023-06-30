package com.revature.Flumblr.utils.custom_exceptions;

public class PostNotFoundException extends RuntimeException  {
    public PostNotFoundException(String postId) {
        super("post " + postId + " not found");
    }
}
