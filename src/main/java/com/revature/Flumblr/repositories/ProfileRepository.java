package com.revature.Flumblr.repositories;
import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

//    Profile findByProfileId(Profile profile);

    Profile getProfileByUser(Profile user_id);

    @Modifying
    @Transactional
    @Query("UPDATE profile p SET p.profile_img = ?1 WHERE p.user = ?2")
    void setProfileImg(String profile_img, User user);

    @Modifying
    @Transactional
    @Query("UPDATE profile p SET p.bio = ?1 WHERE p.user = ?2")
    Profile setBio(String bio, User user);

    Profile getProfileByUser(User user);

}
