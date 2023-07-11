package com.revature.Flumblr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.Flumblr.entities.Bookmark;
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.User;

@Repository
public interface BookmarksRepository extends JpaRepository<Bookmark, String> {

    Optional<Bookmark> findByUserAndPost(User user, Post post);
}
