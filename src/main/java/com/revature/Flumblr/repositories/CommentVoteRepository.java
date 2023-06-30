package com.revature.Flumblr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.Comment;
import com.revature.Flumblr.entities.CommentVote;
import com.revature.Flumblr.entities.User;

public interface CommentVoteRepository extends JpaRepository<CommentVote, String> {
    Optional<CommentVote> findByUserAndComment(User user, Comment comment);
}
