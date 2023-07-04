package com.revature.Flumblr.repositories;
import com.revature.Flumblr.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findById(String profileId);

    @Modifying
    @Query("UPDATE profile p SET p.profile_Img = ?1 WHERE p.user_id = ?2")
    Profile setProfileImg(byte[] profileImg, String user_id);

    @Modifying
    @Query("UPDATE profile p SET p.bio = ?1 WHERE p.user_id = ?2")
    Profile setBio(String bio, String user_id);

    Profile getProfileByUser(String user_id);

}
