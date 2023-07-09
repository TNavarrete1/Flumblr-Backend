package com.revature.Flumblr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.Flumblr.entities.PostShare;

@Repository
public interface PostShareRepository extends JpaRepository<PostShare, String> {
    Optional<PostShare> findByPostIdAndUserId(String postId, String userId);
}
