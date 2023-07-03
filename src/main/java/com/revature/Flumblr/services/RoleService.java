package com.revature.Flumblr.services;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.Role;
import com.revature.Flumblr.repositories.RoleRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

/**
 * The RoleService class provides operations related to role management.
 */
@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    /**
     * Finds a role by its name.
     *
     * @param name the name of the role to find
     * @return the Role object with the specified name
     * @throws ResourceNotFoundException if the role with the specified name is not
     *                                   found
     */
    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + name + " not found"));
    }

    /**
     * Checks if a role with the specified name already exists.
     *
     * @param name the name to check for uniqueness
     * @return true if the role name is unique, false otherwise
     */
    public boolean isUniqueRole(String name) {
        return roleRepository.findByName(name).isEmpty();
    }
}
