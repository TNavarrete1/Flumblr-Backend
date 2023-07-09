package com.revature.Flumblr.services;

import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.repositories.*;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    // private PostService postService;

    // @Mock
    // private PostRepository postRepository;

    // @Mock
    // private UserService userService;

    // @Mock
    // private UserRepository userRepository;

    // @Mock
    // private S3StorageService s3StorageService;

    // private User user;

    // private Post post;

    // private User followed;

    // private static final String userId = "51194080-3452-4503-b271-6df469cb7983";

    // @BeforeEach
    // public void setup() {
    // postService = new PostService(postRepository, userService, userRepository,
    // s3StorageService);
    // user = new User();
    // followed = new User();
    // user.setId(userId);
    // followed.setUsername("followable");
    // post = new Post("testPost", null, null, user);
    // }

    // @Test
    // public void getFollowingTest() {
    // when(userService.findById(userId)).thenReturn(user);
    // Set<Follow> follows = new HashSet<Follow>();
    // follows.add(new Follow(user, followed));
    // user.setFollows(follows);
    // List<Post> posts = new ArrayList<Post>();
    // posts.add(post);
    // when(postRepository.findAllByUserIn(anyList(),
    // isA(Pageable.class))).thenReturn(posts);
    // postService.getFollowing(userId, 1);
    // verify(postRepository, times(1)).findAllByUserIn(anyList(),
    // isA(Pageable.class));
    // }

    // @Test
    // public void getFeedTest() {
    // List<Post> posts = new ArrayList<Post>();
    // posts.add(post);
    // postService.getFeed(1);
    // verify(postRepository, times(1)).findAllBy(isA(Pageable.class));
    // }

    // @Test
    // public void findByTagTest() {
    // List<String> tagStrings = new ArrayList<String>();
    // tagStrings.add("car");
    // List<Post> posts = new ArrayList<Post>();
    // Set<Tag> tags = new HashSet<Tag>();
    // tags.add(new Tag("car"));
    // post.setTags(tags);
    // posts.add(post);
    // when(postRepository.findAllByTagsNameIn(eq(tagStrings),
    // isA(Pageable.class))).thenReturn(posts);
    // List<Post> resPosts = postService.findByTag(tagStrings, 1);
    // assertEquals("testPost", resPosts.get(0).getMessage());
    // verify(postRepository, times(1)).findAllByTagsNameIn(eq(tagStrings),
    // isA(Pageable.class));
    // }

    // @Test
    // public void getUserPostsTest () {
    // postService.getUserPosts(userId);
    // verify(postRepository, times(1))
    // .findByUserIdOrderByCreateTimeDesc(userId);
    // }

    // @Test
    // public void findByIdTest() {
    // String postId = post.getId();
    // String noPostId = "ac997ca0-852e-4b7b-b9c7-94f47cf38969";
    // when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    // when(postRepository.findById(noPostId)).thenReturn(Optional.empty());
    // assertThrows(ResourceNotFoundException.class, ()->{
    // postService.findById(noPostId);
    // });
    // assertEquals(post, postService.findById(postId));
    // verify(postRepository, times(2)).findById(anyString());
    // }

}
