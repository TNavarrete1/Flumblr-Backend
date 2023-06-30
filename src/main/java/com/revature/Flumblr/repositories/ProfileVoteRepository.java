package com.revature.Flumblr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.ProfileVote;
import com.revature.Flumblr.entities.User;

public interface ProfileVoteRepository extends JpaRepository<ProfileVote, String> {
    Optional<ProfileVote> findByUserAndComment(User user, Profile profile);
}
