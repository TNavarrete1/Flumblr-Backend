package com.revature.Flumblr.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.Flumblr.services.FollowService;
import com.revature.Flumblr.services.TokenService;

@ExtendWith(MockitoExtension.class)
class FollowControllerTest {
    private FollowController followController;

    @Mock
    private FollowService followService;

    @Mock
    private TokenService tokenService;
    private static final String userId = "51194080-3452-4503-b271-6df469cb7983";

    @BeforeEach
    public void setup() {
        followController = new FollowController(followService, tokenService);
        when(tokenService.extractUserId("dummyToken")).thenReturn(userId);
    }

    @Test
    public void createFollowTest() {
        ResponseEntity<?> result = followController.createFollow("dummyToken", "followed");
        verify(followService, times(1)).create(userId, "followed");
        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void deleteFollowTest() {
        ResponseEntity<?> result = followController.deleteFollow("dummyToken", "followed");
        verify(followService, times(1)).delete(userId, "followed");
        assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void getFollowsTest() {
        List<String> followNames = new ArrayList<String>();
        followNames.add("followed");
        followNames.add("popularUser");

        when(followService.findAllByUserId(userId)).thenReturn(followNames);
        ResponseEntity<List<String>> result = followController.getFollows("dummyToken");

        verify(followService, times(1)).findAllByUserId(userId);

        assertEquals(result.getStatusCode(), HttpStatus.OK);

        List<String> resNames = result.getBody();
        assertTrue(resNames.contains("followed"));
        assertTrue(resNames.contains("popularUser"));
        assertEquals(resNames.size(), 2);
    }
}
