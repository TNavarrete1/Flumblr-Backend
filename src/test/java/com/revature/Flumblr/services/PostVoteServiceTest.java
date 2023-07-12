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

import com.revature.Flumblr.dtos.requests.PostVoteRequest;
import com.revature.Flumblr.entities.NotificationType;
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.PostVote;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.PostVoteRepository;

public class PostVoteServiceTest {
    @Mock
    PostVoteRepository postVoteRepository;

    @Mock
    UserService userService;

    @Mock
    PostService postService;

    @Mock
    NotificationService notificationService;

    @Mock
    NotificationTypeService notificationTypeService;

    @InjectMocks
    PostVoteService postVoteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDelete() {
        PostVote vote = new PostVote();

        postVoteService.delete(vote);

        verify(postVoteRepository).delete(vote);
    }

    @Test
    void testFindById() {
        String id = "a real id";
        PostVote vote = new PostVote();
        vote.setId(id);

        when(postVoteRepository.findById(id)).thenReturn(java.util.Optional.of(vote));

        PostVote result = postVoteService.findById(id);

        assertEquals(vote, result);
    }

    @Test
    void testFindByUserAndPost() {
        User user = new User();
        Post post = new Post();
        PostVote postVote = new PostVote();

        when(postVoteRepository.findByUserAndPost(user, post)).thenReturn(java.util.Optional.of(postVote));

        PostVote result = postVoteService.findByUserAndPost(user, post);

        assertEquals(postVote, result);
    }

    @Test
    void testSave() {
        PostVote vote = new PostVote();
        postVoteService.save(vote);
        verify(postVoteRepository).save(vote);
    }

    @Test
    void testVote() {
        PostVoteRequest req = new PostVoteRequest();
        req.setPostId("postId");
        req.setUserId("userId");
        User user = new User();
        user.setId(req.getUserId());
        user.setUsername("user");
        Post post = new Post();
        post.setId(req.getPostId());
        PostVote vote = new PostVote();
        vote.setPost(post);
        vote.setUser(user);
        vote.setVote(false);

        NotificationType type = new NotificationType();
        type.setName("postLike");

        when(userService.findById(req.getUserId())).thenReturn(user);
        when(postService.findById(req.getPostId())).thenReturn(post);
        when(postVoteRepository.findByUserAndPost(user, post)).thenReturn(java.util.Optional.empty());
        when(notificationTypeService.findByName("postLike")).thenReturn(type);

        postVoteService.vote(req);

        verify(postVoteRepository).save(any(PostVote.class));
        verify(notificationService).createNotification(eq("user voted on your post"), eq("post:" + req.getPostId()),
                eq(post.getUser()),
                any(NotificationType.class));
    }
}
