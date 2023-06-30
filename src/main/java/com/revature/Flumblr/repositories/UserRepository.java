package com.revature.Flumblr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The PostRepository interface provides database operations for Post entities.
 */
import com.revature.Flumblr.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
