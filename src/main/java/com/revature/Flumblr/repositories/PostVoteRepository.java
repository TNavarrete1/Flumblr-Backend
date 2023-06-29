package com.revature.Flumblr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.PostVote;

public interface PostVoteRepository extends JpaRepository<PostVote, String> {
    Optional<PostVote> findByUserAndPost(User user, Post post);
}
