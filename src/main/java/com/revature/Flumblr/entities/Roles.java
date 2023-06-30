package com.revature.Flumblr.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Id;


public class Roles {
    @Id
    private String id;

    // set the column username to username
    @Column(name = "name", nullable = false)
    private String name;   
}
