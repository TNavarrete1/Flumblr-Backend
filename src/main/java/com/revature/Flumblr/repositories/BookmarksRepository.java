package com.revature.Flumblr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.Flumblr.entities.Bookmark;

@Repository
public interface BookmarksRepository extends JpaRepository<Bookmark, String> {
}
