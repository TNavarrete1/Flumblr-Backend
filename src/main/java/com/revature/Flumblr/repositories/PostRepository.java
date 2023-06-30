package com.revature.Flumblr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The PostRepository interface provides database operations for Post entities.
 */
import com.revature.Flumblr.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> getByUserIdOrderByCreateTimeDesc(String userId);
}
