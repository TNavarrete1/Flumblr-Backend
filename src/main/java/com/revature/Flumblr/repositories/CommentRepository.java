package com.revature.Flumblr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import java.util.List;

/**
 * The PostRepository interface provides database operations for Post entities.
 */
import com.revature.Flumblr.entities.Comment;
import com.revature.Flumblr.entities.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

     List <Comment> findAllByPost(Post post);
}
