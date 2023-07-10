package com.revature.Flumblr.entities;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;


import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="verifications")
public class Verification {

    @Id
    private String id;

    @Column(name="verification_token")
    private String verificationToken;

    @Column()
    private Date verificationDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Verification(User user) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.verificationDate = new Date();
        this.verificationToken = UUID.randomUUID().toString();
    }

}