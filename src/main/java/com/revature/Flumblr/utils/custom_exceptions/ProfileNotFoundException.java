package com.revature.Flumblr.utils.custom_exceptions;

public class ProfileNotFoundException extends RuntimeException {

    public ProfileNotFoundException(String message) {
        super(message);
    }

}
