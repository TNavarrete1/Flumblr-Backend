package com.revature.Flumblr.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import jakarta.transaction.Transactional;


import org.springframework.stereotype.Service;

import com.revature.Flumblr.repositories.FollowRepository;
import com.revature.Flumblr.repositories.ProfileRepository;

import com.revature.Flumblr.dtos.responses.PotentialFollowerResponse;
import com.revature.Flumblr.entities.Follow;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    private final UserService userService;
    private final NotificationService notificationService;
    private final NotificationTypeService notificationTypeService;
    private final ProfileRepository profileRepository;
    public boolean doesFollow(String userId, String followName) {
        Optional<Follow> followOpt = followRepository.findByUserIdAndFollowUsername(userId, followName);
        return (!followOpt.isEmpty());
    }

    // returns usernames
    public List<String> findAllByUserId(String userId) {
        List<String> follows = new ArrayList<String>();
        User user = userService.findById(userId);
        for (Follow follow : user.getFollows()) {
            follows.add(follow.getFollow().getUsername());
        }
        return follows;
    }

    // followName is the username of the person followed
    @Transactional
    public void delete(String userId, String followName) {
        if (!doesFollow(userId, followName))
            throw new ResourceConflictException("can't unfollow: user " + userId +
                    " doesn't follow " + followName);
        followRepository.deleteByUserIdAndFollowUsername(userId, followName);
    }

    // followName is the username of the person followed
    public void create(String userId, String followName) {
        if (doesFollow(userId, followName))
            throw new ResourceConflictException("can't follow: user " + userId +
                    " already follows " + followName);
        User follower = userService.findById(userId);
        User followed = userService.findByUsername(followName);
        Follow follow = new Follow(follower, followed);
        followRepository.save(follow);
        notificationService.createNotification("User " + follower.getUsername() + " followed you",
                "user:" + follower.getId(), followed, notificationTypeService.findByName("follow"));
    }

    public List<PotentialFollowerResponse> getPotentialListOfFollowers(String[] Tag) {
        List<PotentialFollowerResponse> collected = new ArrayList<>();
        for (String singleTag : Tag) {
            Optional<List<PotentialFollowerResponse>> listofFollowers = Optional.of(
            new ArrayList<>(profileRepository.getPotentialListOfFollowers(singleTag)));
            collected.addAll(listofFollowers.get());
        }
        return collected;
    }
}
