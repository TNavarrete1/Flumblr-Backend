package com.revature.Flumblr.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.Flumblr.dtos.requests.CommentVoteRequest;
import com.revature.Flumblr.dtos.requests.PostVoteRequest;
import com.revature.Flumblr.dtos.requests.ProfileVoteRequest;
import com.revature.Flumblr.services.CommentVoteService;
import com.revature.Flumblr.services.PostVoteService;
import com.revature.Flumblr.services.ProfileVoteService;
import com.revature.Flumblr.services.TokenService;

public class VoteControllerTest {
    @Mock
    TokenService tokenService;

    @Mock
    PostVoteService postVoteService;

    @Mock
    CommentVoteService commentVoteService;

    @Mock
    ProfileVoteService profileVoteService;

    @InjectMocks
    VoteController voteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVoteComment() {
        CommentVoteRequest req = new CommentVoteRequest();
        req.setCommentId("post");
        req.setUserId("user");
        req.setVote(false);

        String token = "valid";

        voteController.voteComment(req, token);

        verify(commentVoteService).vote(req);
    }

    @Test
    void testVotePost() {
        PostVoteRequest req = new PostVoteRequest();
        req.setPostId("post");
        req.setUserId("user");
        req.setVote(false);

        String token = "valid";

        voteController.votePost(req, token);

        verify(postVoteService).vote(req);
    }

    @Test
    void testVoteProfile() {
        ProfileVoteRequest req = new ProfileVoteRequest();
        req.setProfileId("post");
        req.setUserId("user");
        req.setVote(false);

        String token = "valid";

        voteController.voteProfile(req, token);

        verify(profileVoteService).vote(req);
    }
}
