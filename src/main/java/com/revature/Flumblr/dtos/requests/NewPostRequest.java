package com.revature.Flumblr.dtos.requests;

import java.util.Date;

import jakarta.persistence.Column;

public class NewPostRequest {

    //make columns
    @Column(nullable = false)
    String id;

    String message;
    String url;

    Date createTime;
    Date editTime;
    String mediaType;

    
}
