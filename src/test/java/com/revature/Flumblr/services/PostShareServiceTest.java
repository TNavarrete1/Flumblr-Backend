package com.revature.Flumblr.services;
import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.repositories.*;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostShareServiceTest {
    private PostShareService postShareService;

    @Mock
    private PostShareRepository postShareRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationTypeService notificationTypeService;

    private User user;

    private User otherUser;

    private User thirdUser;

    private Post post;

    private PostShare postShare;

    private PostShare existingShare;

    private static final String userId = "51194080-3452-4503-b271-6df469cb7983";

    private static final String otherUserId = "860d9e34-7a60-40e6-8005-12d0722202bf";

    @BeforeEach
    public void setup() {
        postShareService = new PostShareService(postShareRepository, userService, postService,
            notificationService, notificationTypeService);
        user = new User();
        user.setId(userId);
        otherUser = new User();
        otherUser.setId(otherUserId);
        thirdUser = new User();
        thirdUser.setId("a1229037-4ecc-4b9c-9da9-913152ad306b");
        post = new Post("testPost", null, null, otherUser);
        postShare = new PostShare(user, post);
        existingShare = new PostShare(thirdUser, post);
    }

    @Test
    public void createTest() {
        when(postShareRepository.findByPostIdAndUserId(post.getId(), thirdUser.getId())).
            thenReturn(Optional.of(existingShare));
        when(postShareRepository.findByPostIdAndUserId(post.getId(), userId)).
            thenReturn(Optional.empty());
        when(postShareRepository.findByPostIdAndUserId(post.getId(), otherUserId)).
            thenReturn(Optional.empty());
        when(userService.findById(userId)).thenReturn(user);
        when(userService.findById(otherUserId)).thenReturn(otherUser);
        when(postService.findById(post.getId())).thenReturn(post);
        assertThrows(ResourceConflictException.class, () -> {
            postShareService.create(post.getId(), thirdUser.getId());
        });
        assertThrows(ResourceConflictException.class, () -> {
            postShareService.create(post.getId(), otherUserId);
        });
        postShareService.create(post.getId(), userId);
        ArgumentCaptor<PostShare> postShareArg = ArgumentCaptor.forClass(PostShare.class);
        verify(postShareRepository, times(1)).save(postShareArg.capture());
        assertEquals(postShareArg.getValue().getPost().getId(), post.getId());
        assertEquals(postShareArg.getValue().getUser().getId(), userId);
    }

    @Test
    public void deleteTest() {
        when(postShareRepository.findByPostIdAndUserId(post.getId(), thirdUser.getId())).
            thenReturn(Optional.empty());
        when(postShareRepository.findByPostIdAndUserId(post.getId(), userId)).
            thenReturn(Optional.of(postShare));
        assertThrows(ResourceConflictException.class, () -> {
            postShareService.delete(post.getId(), thirdUser.getId());
        });
        postShareService.delete(post.getId(), userId);
        ArgumentCaptor<PostShare> postShareArg = ArgumentCaptor.forClass(PostShare.class);
        verify(postShareRepository, times(1)).delete(postShareArg.capture());
        assertEquals(postShareArg.getValue().getId(), postShare.getId());
    }
}
