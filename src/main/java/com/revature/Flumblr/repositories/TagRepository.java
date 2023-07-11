package com.revature.Flumblr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {

    Optional<Tag> findByName(String name);

}
