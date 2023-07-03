package com.revature.Flumblr.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.repositories.FollowRepository;
import com.revature.Flumblr.entities.Follow;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowService {
    private final FollowRepository followRepo;
    private final UserService userService;

    public boolean doesFollow(String userId, String followName) {
        Optional<Follow> followOpt = followRepo.findByUserIdAndFollowUsername(userId, followName);
        return(!followOpt.isEmpty());
    }

    // returns usernames
    public List<String> getAllForUser(String userId) {
        List<String> follows = new ArrayList<String>();
        User user = userService.getUserById(userId);
        for(Follow follow : user.getFollows()) {
            follows.add(follow.getFollow().getUsername());
        }
        return follows;
    }
    
    // followName is the username of the person followed
    @Transactional
    public void deleteFollow(String userId, String followName) {
        if(!doesFollow(userId, followName)) throw new ResourceConflictException("can't unfollow: user " + userId +
            " doesn't follow " + followName);
        followRepo.deleteByUserIdAndFollowUsername(userId, followName);
    }

    // followName is the username of the person followed
    public void newFollow(String userId, String followName) {
        if(doesFollow(userId, followName)) throw new ResourceConflictException("can't follow: user " + userId +
            " already follows " + followName);
        User follower = userService.getUserById(userId);
        User followed = userService.getUserByUsername(followName);
        Follow follow = new Follow(follower, followed);
        followRepo.save(follow);
    }
}
