package com.revature.Flumblr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.Flumblr.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    Post getReferenceById(String id);
}
