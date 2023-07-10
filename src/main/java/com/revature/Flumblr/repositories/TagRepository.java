package com.revature.Flumblr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.revature.Flumblr.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {

    Optional<Tag> findByName(String name);

    @Query(value = "SELECT name FROM tags JOIN (SELECT tag_id,  FROM " +
    "post_tag_list JOIN posts ON post_id = posts.id GROUP BY tag_id)", nativeQuery = true)
    List<String> findTrending();

}
