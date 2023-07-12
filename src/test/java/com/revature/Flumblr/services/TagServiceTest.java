package com.revature.Flumblr.services;

import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.repositories.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostRepository postRepository;
    private static final String userId = "51194080-3452-4503-b271-6df469cb7983";
    private static final String otherUserId = "3e1a9ffe-c80f-4be2-a237-d2c1299f6f06";

    private User user;
    private User otherUser;

    private Set<PostShare> postShares;

    private Set<PostVote> postVotes;

    private List<Comment> postComments;

    @BeforeEach
    public void setup() {
        postShares = new HashSet<PostShare>();
        postVotes = new HashSet<PostVote>();
        postComments = new ArrayList<Comment>();
        tagService = new TagService(tagRepository, postRepository);
        user = new User();
        otherUser = new User();
        user.setId(userId);
        otherUser.setId(otherUserId);
    }

    @Test
    public void findByNameExistTest() {
        String name = "realTag";
        Tag tag = new Tag(name);
        when(tagRepository.findByName(name)).thenReturn(java.util.Optional.of(tag));

        Tag result = tagService.findByName(name);

        assertEquals(tag, result);
    }

    @Test
    public void findByNameNotExistTest() {
        String name = "realTag";
        Tag tag = new Tag(name);
        when(tagRepository.findByName(name)).thenReturn(java.util.Optional.empty());

        Tag result = tagService.findByName(name);

        assertEquals(tag.getName(), result.getName());
    }

    @Test
    void createTagTest() {
        String name = "realTag";
        Tag result = tagService.createTag(name);
        verify(tagRepository).save(result);
    }

    @Test
    public void getTrendingTest() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date inDate = null;
        try {
            inDate = formatter.parse("29-06-2023");
        } catch (Exception e) {
            fail();
        }
        Set<Tag> post1Tags = new HashSet<Tag>();
        Tag catTag = new Tag("cat");
        Tag dogTag = new Tag("dog");
        post1Tags.add(dogTag);
        Set<Tag> post2Tags = new HashSet<Tag>();
        post2Tags.add(catTag);
        Set<Tag> post3Tags = new HashSet<Tag>();
        post3Tags.add(catTag);
        List<Post> trendPosts = new ArrayList<Post>();
        Post post1 = new Post("#dog", "none", null, user, post1Tags);
        post1.setPostShares(postShares);
        post1.setPostVotes(postVotes);
        post1.setComments(postComments);
        Post post2 = new Post("#cat", "none", null, otherUser, post2Tags);
        post2.setPostShares(postShares);
        post2.setPostVotes(postVotes);
        post2.setComments(postComments);
        Post post3 = new Post("another #cat post", "none", null, user, post3Tags);
        post3.setPostShares(postShares);
        post3.setPostVotes(postVotes);
        post3.setComments(postComments);
        trendPosts.add(post1);
        trendPosts.add(post2);
        trendPosts.add(post3);
        when(postRepository.findAllWithTagsAfter(inDate)).thenReturn(trendPosts);
        List<String> resTags = tagService.getTrending(inDate);
        assertEquals(resTags, Arrays.asList("cat", "dog"));
        verify(postRepository, times(1)).findAllWithTagsAfter(inDate);
    }

}
