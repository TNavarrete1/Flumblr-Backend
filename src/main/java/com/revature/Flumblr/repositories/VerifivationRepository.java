package com.revature.Flumblr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.Verification;

public interface VerifivationRepository extends JpaRepository<Verification, String> {

    Verification findByVerificationToken(String confirmationToken);
    
}
