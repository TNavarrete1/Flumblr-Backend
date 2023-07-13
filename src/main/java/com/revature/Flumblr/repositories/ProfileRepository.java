package com.revature.Flumblr.repositories;

import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.Theme;
import com.revature.Flumblr.entities.User;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Profile getProfileByUser(User user);

    @Modifying
    @Transactional
    @Query("UPDATE Profile p SET p.profile_img = :profileImg WHERE p.id = :id")
    void setProfileImg(String id, String profileImg);

    @Modifying
    @Transactional
    @Query("UPDATE Profile p SET p.bio = :bio WHERE p.id = :id")
    void setBio(String id, String bio);

    @Modifying
    @Transactional
    @Query("UPDATE Profile p SET p.theme = :theme WHERE p.id = :id")
    void setTheme(String id, Theme theme);


    @Query(value = "SELECT p.id, p.bio, p.profile_img, u.username FROM profile p \n" + //
            "JOIN users u ON p.user_id = u.id \n" + //
            "JOIN profile_tag_list ptl ON ptl.profile_id = p.id \n" + //
            "JOIN tags t ON t.id = ptl.tag_id \n" + //
            "join posts p2 on u.id = p2.user_id \n" + //
            "join post_tag_list ptl2 on ptl2.post_id  = p2.id  \n" + //
            "JOIN tags t2 ON t2.id = ptl2.tag_id\n" + //
            "where t2.name = :tag or t.name = :tag", nativeQuery = true)
    List<Object[]> getPotentialListOfFollowers(@Param("tag") String tag);

    @Query(value = "SELECT p.profile_img FROM profile p WHERE p.id = :id", nativeQuery = true)
    String getOldProfileUrl(String id);

   

    @Query(value = "SELECT p.id, p.bio, p.profile_img, u.username FROM profile p JOIN users u ON p.user_id = u.id WHERE u.username LIKE %:username%", nativeQuery = true)
    List<Object[]> getPotentialListOfUsersByUsername(@Param("username") String username);

}