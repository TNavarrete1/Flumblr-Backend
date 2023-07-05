package com.revature.Flumblr.utils.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;

@Configuration
public class S3Config {

    @Value("${access-key}")
    private String access_key;

    @Value("${secret-key}")
    private String secret_key;

    @Value("${region}")
    private String region;

    // private AmazonS3 generateS3Client


    
}
