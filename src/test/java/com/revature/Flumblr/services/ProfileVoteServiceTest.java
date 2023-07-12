package com.revature.Flumblr.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.Flumblr.dtos.requests.ProfileVoteRequest;
import com.revature.Flumblr.entities.NotificationType;
import com.revature.Flumblr.entities.Profile;
import com.revature.Flumblr.entities.ProfileVote;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.ProfileVoteRepository;

public class ProfileVoteServiceTest {
    @Mock
    ProfileVoteRepository profileVoteRepository;

    @Mock
    UserService userService;

    @Mock
    ProfileService profileService;

    @Mock
    NotificationService notificationService;

    @Mock
    NotificationTypeService notificationTypeService;

    @InjectMocks
    ProfileVoteService profileVoteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDelete() {
        ProfileVote vote = new ProfileVote();

        profileVoteService.delete(vote);

        verify(profileVoteRepository).delete(vote);
    }

    @Test
    void testFindById() {
        String id = "a real id";
        ProfileVote vote = new ProfileVote();
        vote.setId(id);

        when(profileVoteRepository.findById(id)).thenReturn(java.util.Optional.of(vote));

        ProfileVote result = profileVoteService.findById(id);

        assertEquals(vote, result);
    }

    @Test
    void testFindByUserAndProfile() {
        User user = new User();
        Profile profile = new Profile();
        ProfileVote profileVote = new ProfileVote();

        when(profileVoteRepository.findByUserAndProfile(user, profile)).thenReturn(java.util.Optional.of(profileVote));

        ProfileVote result = profileVoteService.findByUserAndProfile(user, profile);

        assertEquals(profileVote, result);
    }

    @Test
    void testSave() {
        ProfileVote vote = new ProfileVote();
        profileVoteService.save(vote);
        verify(profileVoteRepository).save(vote);
    }

    @Test
    void testVote() {
        ProfileVoteRequest req = new ProfileVoteRequest();
        req.setProfileId("profileId");
        req.setUserId("userId");
        User user = new User();
        user.setId(req.getUserId());
        user.setUsername("user");
        Profile profile = new Profile();
        profile.setId(req.getProfileId());
        ProfileVote vote = new ProfileVote();
        vote.setProfile(profile);
        vote.setUser(user);
        vote.setVote(false);

        NotificationType type = new NotificationType();
        type.setName("profileLike");

        when(userService.findById(req.getUserId())).thenReturn(user);
        when(profileService.findById(req.getProfileId())).thenReturn(profile);
        when(profileVoteRepository.findByUserAndProfile(user, profile)).thenReturn(java.util.Optional.empty());
        when(notificationTypeService.findByName("profileLike")).thenReturn(type);

        profileVoteService.vote(req);

        verify(profileVoteRepository).save(any(ProfileVote.class));
        verify(notificationService).createNotification(eq("user voted on your profile"),
                eq("profile:" + req.getProfileId()),
                eq(profile.getUser()),
                any(NotificationType.class));
    }
}
