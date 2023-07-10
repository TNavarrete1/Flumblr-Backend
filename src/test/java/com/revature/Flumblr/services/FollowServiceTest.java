package com.revature.Flumblr.services;

import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.repositories.*;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {
    private FollowService followService;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationTypeService notificationTypeService;

    @Mock
    private ProfileRepository profileRepository;

    private User user;

    private User followed;

    private User unFollowed;

    private static final String userId = "51194080-3452-4503-b271-6df469cb7983";

    @BeforeEach
    public void setup() {
        followService = new FollowService(
            followRepository, 
            userService, 
            notificationService, 
            notificationTypeService, 
            profileRepository
        );
        user = new User();
        followed = new User();
        unFollowed = new User();
        user.setId(userId);
        followed.setUsername("followable");
        unFollowed.setUsername("unfollowable");
    }

    @Test
    public void createTest() {
        when(followRepository.findByUserIdAndFollowUsername(userId,
            "unfollowable")).thenReturn(Optional.empty());
        when(followRepository.findByUserIdAndFollowUsername(userId,
            "followable")).thenReturn(Optional.of(new Follow(user, followed)));
        when(userService.findById(userId)).thenReturn(user);
        when(userService.findByUsername("unfollowable")).thenReturn(unFollowed);
        assertThrows(ResourceConflictException.class, ()->{
            followService.create(userId, "followable");
        });
        followService.create(userId, "unfollowable");
        verify(followRepository, times(1)).
            save(isA(Follow.class));
    }

    @Test
    public void doesFollowTest() {
        when(followRepository.findByUserIdAndFollowUsername(userId,
            "unfollowable")).thenReturn(Optional.empty());
        when(followRepository.findByUserIdAndFollowUsername(userId,
            "followable")).thenReturn(Optional.of(new Follow(user, followed)));
        assertTrue(followService.doesFollow(userId, "followable"));
        verify(followRepository, times(1)).findByUserIdAndFollowUsername(userId,
            "followable");
        assertFalse(followService.doesFollow(userId, "unfollowable"));
        verify(followRepository, times(1)).findByUserIdAndFollowUsername(userId,
            "unfollowable");
    }

    @Test
    public void findAllByUserIdTest() {
        when(userService.findById(userId)).thenReturn(user);
        Set<Follow> follows = new HashSet<Follow>();
        // unFollowed is actually followed here
        follows.add(new Follow(user, unFollowed));
        follows.add(new Follow(user, followed));
        user.setFollows(follows);
        List<String> followNames = followService.findAllByUserId(userId);
        assertTrue(followNames.contains("followable"));
        assertTrue(followNames.contains("unfollowable"));
        assertEquals(followNames.size(), 2);
    }

    @Test
    public void deleteTest() {
        when(followRepository.findByUserIdAndFollowUsername(userId,
            "unfollowable")).thenReturn(Optional.empty());
        when(followRepository.findByUserIdAndFollowUsername(userId,
            "followable")).thenReturn(Optional.of(new Follow(user, followed)));
        assertThrows(ResourceConflictException.class, ()->{
            followService.delete(userId, "unfollowable");
        });
        followService.delete(userId, "followable");
        verify(followRepository, times(1)).
            deleteByUserIdAndFollowUsername(userId, "followable");
    }

}
