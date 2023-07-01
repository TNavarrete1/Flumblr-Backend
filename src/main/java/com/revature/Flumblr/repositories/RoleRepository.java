package com.revature.Flumblr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.Flumblr.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role getReferenceById(String id);
}
