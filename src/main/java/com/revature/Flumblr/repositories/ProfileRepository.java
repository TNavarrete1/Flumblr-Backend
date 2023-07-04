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

//    @Modifying
//    @Query("UPDATE profile p SET p.profile_img = ?1 WHERE p.user_id = ?2")
//    Profile setProfileImg(byte[] profile_img, User user);

    @Modifying
    @Query("UPDATE profile p SET p.profile_img = ?1 WHERE p.user_id = ?2")
    void setProfileImg(byte[] profile_img, User user);

    @Modifying
    @Query("UPDATE profile p SET p.bio = ?1 WHERE p.user_id = ?2")
    Profile setBio(String bio, User user);

    Profile getProfileByUser(String user_id);

}
