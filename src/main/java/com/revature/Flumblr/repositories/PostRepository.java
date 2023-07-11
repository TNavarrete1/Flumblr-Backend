package com.revature.Flumblr.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.User;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUserId(String userId);

    @Query("SELECT DISTINCT p FROM Post p left join p.postShares ps " +
            "WHERE p.user.id = :userId OR ps.user.id = :userId "
            + "ORDER BY p.createTime DESC")
    List<Post> findPostsAndSharesByUserId(@Param("userId") String userId);

    @Query("SELECT DISTINCT p FROM Post p left join p.postShares ps " +
            "WHERE p.user IN :following OR ps.user IN :following")
    List<Post> findPostsAndSharesForUserIn(@Param("following") List<User> following, Pageable pageable);

    List<Post> findByTagsNameIn(List<String> tags, Pageable pageable);

    // get all posts that have tags
    @Query("SELECT p FROM Post p WHERE p.createTime >= :dateBefore AND size(p.tags) > 0")
    List<Post> findAllWithTagsAfter(@Param("dateBefore") Date dateBefore);

    List<Post> findByCreateTimeGreaterThanEqual(Date fromDate);

    List<Post> findAllBy(Pageable pageable);
}
