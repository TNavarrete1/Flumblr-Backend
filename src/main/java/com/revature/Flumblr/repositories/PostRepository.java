package com.revature.Flumblr.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.User;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUserIdOrderByCreateTimeDesc(String userId);

    List<Post> findAllByUserIn(List<User> following, Pageable pageable);

    List<Post> findAllByTagsNameIn(List<String> tags, Pageable pageable);

    List<Post> findByCreateTimeGreaterThanEqual(Date fromDate);

    List<Post> findAllBy(Pageable pageable);
}
