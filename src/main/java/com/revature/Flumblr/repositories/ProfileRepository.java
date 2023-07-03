package com.revature.Flumblr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {

}
