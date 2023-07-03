package com.revature.Flumblr.repositories;
import com.revature.Flumblr.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findById(String profileId);
    Profile update(Profile profile);

}
