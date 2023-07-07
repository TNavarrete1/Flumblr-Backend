package com.revature.Flumblr.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.Flumblr.dtos.responses.PostResponse;
import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.services.*;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    private PostController postController;

    @Mock
    private TokenService tokenService;

    @Mock
    private PostService postService;

    @Mock
    private CommentService commentService;

    @Mock
    private S3StorageService s3StorageService;

    private static final String userId = "51194080-3452-4503-b271-6df469cb7983";

    private List<Post> posts;
    private User user;
    private List<Comment> comments;

    @BeforeEach
    public void setup() {
        postController = new PostController(tokenService, postService, commentService, s3StorageService);
        user = new User();
        // necessary for PostResponse
        user.setProfile(new Profile(user, null, "I'm a teapot"));
        posts = new ArrayList<Post>();
        comments = new ArrayList<Comment>();
        Post addPost = new Post("testPost", null, null, user);
        addPost.setComments(comments);
        posts.add(addPost);
        addPost = new Post("anotherPost", null, null, user);
        addPost.setComments(comments);
        posts.add(addPost);
        when(tokenService.extractUserId("dummyToken")).thenReturn(userId);
    }

    @Test
    public void getFeedTest() {
        assertThrows(BadRequestException.class, ()->{
            postController.getFeed("dummyToken", 0);
        });
        when(postService.getFeed(userId, 0)).thenReturn(posts);

        ResponseEntity<List<PostResponse>> result = postController.getFeed("dummyToken", 1);

        verify(postService, times(1)).getFeed(userId, 0);
        assertEquals(result.getStatusCode(), HttpStatus.OK);

        List<String> resultMessages = new ArrayList<String>();
        for(PostResponse response : result.getBody()) {
            resultMessages.add(response.getMessage());
        }
        assertTrue(resultMessages.contains("testPost"));
        assertTrue(resultMessages.contains("anotherPost"));
        assertEquals(resultMessages.size(), 2);
    }

    @Test
    public void getByTagsTest() {
        List<String> tagStrings = new ArrayList<String>();
        tagStrings.add("sunny");
        tagStrings.add("car");
        assertThrows(BadRequestException.class, ()->{
            postController.getByTags("dummyToken", 0, tagStrings);
        });
        when(postService.findByTag(tagStrings, 0)).thenReturn(posts);

        ResponseEntity<List<PostResponse>> result = postController.getByTags("dummyToken", 1, tagStrings);

        verify(postService, times(1)).findByTag(tagStrings, 0);
        assertEquals(result.getStatusCode(), HttpStatus.OK);

        List<String> resultMessages = new ArrayList<String>();
        for(PostResponse response : result.getBody()) {
            resultMessages.add(response.getMessage());
        }
        assertTrue(resultMessages.contains("testPost"));
        assertTrue(resultMessages.contains("anotherPost"));
        assertEquals(resultMessages.size(), 2);
    }

    @Test
    public void getUserPostsTest() {
        when(postService.getUserPosts(userId)).thenReturn(posts);

        ResponseEntity<List<PostResponse>> result = postController.getUserPosts(userId, "dummyToken");

        verify(postService, times(1)).getUserPosts(userId);
        assertEquals(result.getStatusCode(), HttpStatus.OK);

        List<String> resultMessages = new ArrayList<String>();
        for(PostResponse response : result.getBody()) {
            resultMessages.add(response.getMessage());
        }
        assertTrue(resultMessages.contains("testPost"));
        assertTrue(resultMessages.contains("anotherPost"));
        assertEquals(resultMessages.size(), 2);
    }

    @Test
    public void getPostTest() {
        Post responsePost = new Post("testPost", null, null, user);
        responsePost.setComments(comments);
        final String postId = "c4030998-a0f5-4850-a951-fb9bfc8dcf50";
        responsePost.setId(postId);
        when(postService.findById(postId)).thenReturn(responsePost);

        ResponseEntity<PostResponse> postResponse = postController.getPost(postId, "dummyToken");
        verify(postService, times(1)).findById(postId);
        assertEquals(postResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(postResponse.getBody().getMessage(), "testPost");
    }
}
