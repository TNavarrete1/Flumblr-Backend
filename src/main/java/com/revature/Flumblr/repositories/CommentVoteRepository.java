package com.revature.Flumblr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.CommentVote;

public interface CommentVoteRepository extends JpaRepository<CommentVote, String> {
    Optional<CommentVote> findByUserAndComment(User user, Comment comment);
}
