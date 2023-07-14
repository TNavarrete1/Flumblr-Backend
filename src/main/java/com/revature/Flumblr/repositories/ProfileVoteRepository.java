package com.revature.Flumblr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.ProfileVote;
import com.revature.Flumblr.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileVoteRepository extends JpaRepository<ProfileVote, String> {
    Optional<ProfileVote> findByUserAndProfile(User user, Profile profile);

    List<ProfileVote> findAllByProfile(Profile profile);

}
