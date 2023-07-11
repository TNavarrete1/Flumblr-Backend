// package com.revature.Flumblr.controllers;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.Set;
// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import com.revature.Flumblr.dtos.responses.PostResponse;
// import com.revature.Flumblr.entities.*;
// import com.revature.Flumblr.services.*;
// import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;

// @ExtendWith(MockitoExtension.class)
// class PostControllerTest {
// private PostController postController;

// @Mock
// private TokenService tokenService;

// @Mock
// private PostService postService;

// @Mock
// private CommentService commentService;

// @Mock
// private PostShareService postShareService;

// @Mock
// private S3StorageService s3StorageService;

// private static final String userId = "51194080-3452-4503-b271-6df469cb7983";

// private List<Post> posts;
// private List<PostResponse> postResponses;
// private User user;
// private List<Comment> comments;
// private Set<PostVote> postVotes;
// private Set<PostShare> postShares;

// @BeforeEach
// public void setup() {
// postController = new PostController(tokenService, postService,
// commentService, postShareService,
// s3StorageService);
// user = new User();
// // necessary for PostResponse
// user.setProfile(new Profile(user, null, "I'm a teapot", null));
// posts = new ArrayList<Post>();
// postResponses = new ArrayList<PostResponse>();
// comments = new ArrayList<Comment>();
// postVotes = new HashSet<PostVote>();

// Post addPost = new Post("testPost", null, null, user, null);
// addPost.setComments(comments);
// addPost.setPostVotes(postVotes);
// addPost.setPostShares(postShares);
// posts.add(addPost);

// addPost = new Post("anotherPost", null, null, user, null);
// addPost.setComments(comments);
// addPost.setPostVotes(postVotes);
// addPost.setPostShares(postShares);
// posts.add(addPost);
// postResponses.add(new PostResponse(addPost));
// when(tokenService.extractUserId("dummyToken")).thenReturn(userId);
// }

// @Test
// public void sharePostTest() {
// String postId = "81b6815f-1e20-4a52-b81d-c663e3ce0380";
// ResponseEntity<?> response = postController.sharePost("dummyToken", postId);
// verify(postShareService, times(1)).create(postId, userId);
// assertEquals(response.getStatusCode(), HttpStatus.CREATED);
// }

// @Test
// public void unSharePostTest() {
// String postId = "81b6815f-1e20-4a52-b81d-c663e3ce0380";
// ResponseEntity<?> response = postController.unSharePost("dummyToken",
// postId);
// verify(postShareService, times(1)).delete(postId, userId);
// assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
// }

// @Test
// public void getFollowingTest() {
// assertThrows(BadRequestException.class, () -> {
// postController.getFollowing("dummyToken", 0);
// });
// when(postService.getFollowing(userId, 0)).thenReturn(postResponses);

// ResponseEntity<List<PostResponse>> result =
// postController.getFollowing("dummyToken", 1);

// verify(postService, times(1)).getFollowing(userId, 0);
// assertEquals(result.getStatusCode(), HttpStatus.OK);

// List<String> resultMessages = new ArrayList<String>();
// for (PostResponse response : result.getBody()) {
// resultMessages.add(response.getMessage());
// }
// assertTrue(resultMessages.contains("testPost"));
// assertTrue(resultMessages.contains("anotherPost"));
// assertEquals(resultMessages.size(), 2);
// }

// @Test
// public void getFeedTest() {
// assertThrows(BadRequestException.class, () -> {
// postController.getFeed("dummyToken", 0);
// });
// when(postService.getFeed(0, userId)).thenReturn(postResponses);

// ResponseEntity<List<PostResponse>> result =
// postController.getFeed("dummyToken", 1);

// verify(postService, times(1)).getFeed(0, userId);
// assertEquals(result.getStatusCode(), HttpStatus.OK);

// List<String> resultMessages = new ArrayList<String>();
// for (PostResponse response : result.getBody()) {
// resultMessages.add(response.getMessage());
// }
// assertTrue(resultMessages.contains("testPost"));
// assertTrue(resultMessages.contains("anotherPost"));
// assertEquals(resultMessages.size(), 2);
// }

// @Test
// public void getByTagsTest() {
// List<String> tagStrings = new ArrayList<String>();
// tagStrings.add("sunny");
// tagStrings.add("car");
// assertThrows(BadRequestException.class, () -> {
// postController.getByTags("dummyToken", 0, tagStrings);
// });
// when(postService.findByTag(tagStrings, 0, userId)).thenReturn(postResponses);

// ResponseEntity<List<PostResponse>> result =
// postController.getByTags("dummyToken", 1, tagStrings);

// verify(postService, times(1)).findByTag(tagStrings, 0, userId);
// assertEquals(result.getStatusCode(), HttpStatus.OK);

// List<String> resultMessages = new ArrayList<String>();
// for (PostResponse response : result.getBody()) {
// resultMessages.add(response.getMessage());
// }
// assertTrue(resultMessages.contains("testPost"));
// assertTrue(resultMessages.contains("anotherPost"));
// assertEquals(resultMessages.size(), 2);
// }

// @Test
// public void getUserPostsTest() {
// when(postService.getUserPosts(userId, userId)).thenReturn(postResponses);

// ResponseEntity<List<PostResponse>> result =
// postController.getUserPosts(userId, "dummyToken");

// verify(postService, times(1)).getUserPosts(userId, userId);
// assertEquals(result.getStatusCode(), HttpStatus.OK);

// List<String> resultMessages = new ArrayList<String>();
// for(PostResponse response : result.getBody()) {
// resultMessages.add(response.getMessage());
// }
// assertTrue(resultMessages.contains("testPost"));
// assertTrue(resultMessages.contains("anotherPost"));
// assertEquals(resultMessages.size(), 2);
// }

// @Test
// public void getPostTest() {
// Post responsePost = new Post("testPost", null, null, user, null);
// responsePost.setComments(comments);
// responsePost.setPostVotes(postVotes);
// responsePost.setPostShares(postShares);
// final String postId = "c4030998-a0f5-4850-a951-fb9bfc8dcf50";
// responsePost.setId(postId);
// PostResponse postResponse = new PostResponse(responsePost);
// when(postService.findByIdResponse(postId, userId)).thenReturn(postResponse);

// ResponseEntity<PostResponse> postResponseEntity =
// postController.getPost(postId,
// "dummyToken");
// verify(postService, times(1)).findByIdResponse(postId, userId);
// assertEquals(postResponseEntity.getStatusCode(), HttpStatus.OK);
// assertEquals(postResponseEntity.getBody().getMessage(), "testPost");
// }
// }
