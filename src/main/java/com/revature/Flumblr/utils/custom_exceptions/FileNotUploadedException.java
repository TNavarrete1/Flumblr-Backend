package com.revature.Flumblr.utils.custom_exceptions;

public class FileNotUploadedException extends RuntimeException {
    public FileNotUploadedException(String message) {
        super(message);
    }
    
}
