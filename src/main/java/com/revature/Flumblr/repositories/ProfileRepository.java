package com.revature.Flumblr.repositories;
import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Profile getProfileByUser(Profile user_id);

    @Modifying
    @Query("UPDATE Profile p SET p.profile_img = :profileImg WHERE p.user = :user")
    Profile setProfileImg(String profileImg, User user);

    @Modifying
    @Query("UPDATE Profile p SET p.bio = :bio WHERE p.user = :user")
    Profile setBio(String bio, User user);

    Profile getProfileByUser(User user_id);

}
