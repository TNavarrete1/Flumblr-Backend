package com.revature.Flumblr.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.Flumblr.entities.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, String> {
    Optional<Follow> findByUserIdAndFollowUsername(String userId, String followName);
    void deleteByUserIdAndFollowUsername(String userId, String followName);
}
