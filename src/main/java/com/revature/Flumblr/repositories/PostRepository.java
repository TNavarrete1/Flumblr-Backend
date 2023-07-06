package com.revature.Flumblr.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The PostRepository interface provides database operations for Post entities.
 */
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.User;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUserIdOrderByCreateTimeDesc(String userId);

    List<Post> findAllByUserIn(List<User> following, Pageable pageable);
}
