package com.revature.Flumblr.repositories;

import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Profile getProfileByUser(User user);

    @Modifying
    @Query("UPDATE Profile p SET p.profile_img = :profileImg WHERE p.id = :id")
    Profile setProfileImg(String id, String profileImg);

    @Modifying
    @Query("UPDATE Profile p SET p.bio = :bio WHERE p.id = :id")
    Profile setBio(String id, String bio);

    @Modifying
    @Query("UPDATE Profile p SET p.theme = :theme WHERE p.id = :id")
    Profile setTheme(String id, String theme);

}
