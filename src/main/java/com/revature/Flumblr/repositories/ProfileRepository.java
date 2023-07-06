package com.revature.Flumblr.repositories;

import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findById(String profileId);

    // @Modifying
    // @Transactional
    // @Query("UPDATE profile p SET p.profile_img = ?1 WHERE p.user = ?2")
    // void setProfileImg(String profile_img, User user);

    // @Modifying
    // @Transactional
    // @Query("UPDATE profile p SET p.bio = ?1 WHERE p.user = ?2")
    // Profile setBio(String bio, User user);

    @Modifying
    // @Transactional
    @Query("UPDATE Profile p SET p.profile_img = :profileImg WHERE p.user = :user")
    void setProfileImg(String profileImg, User user);

    @Modifying
    // @Transactional
    @Query("UPDATE Profile p SET p.bio = :bio WHERE p.user = :user")
    Profile setBio(String bio, User user);

    Profile getProfileByUser(User user_id);

}
