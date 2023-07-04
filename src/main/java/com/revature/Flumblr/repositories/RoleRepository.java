package com.revature.Flumblr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.Flumblr.entities.Role;

/**
 * The RoleRepository interface provides database operations for Role entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * Finds a role by its name.
     *
     * @param name the name of the role to search for
     * @return an Optional containing the Role object if found, or an empty Optional
     *         otherwise
     */
    Optional<Role> findByName(String name);
}
