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

import com.revature.Flumblr.dtos.requests.CommentVoteRequest;
import com.revature.Flumblr.entities.NotificationType;
import com.revature.Flumblr.entities.Comment;
import com.revature.Flumblr.entities.CommentVote;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.CommentVoteRepository;

public class CommentVoteServiceTest {
    @Mock
    CommentVoteRepository commentVoteRepository;

    @Mock
    UserService userService;

    @Mock
    CommentService commentService;

    @Mock
    NotificationService notificationService;

    @Mock
    NotificationTypeService notificationTypeService;

    @InjectMocks
    CommentVoteService commentVoteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDelete() {
        CommentVote vote = new CommentVote();

        commentVoteService.delete(vote);

        verify(commentVoteRepository).delete(vote);
    }

    @Test
    void testFindById() {
        String id = "a real id";
        CommentVote vote = new CommentVote();
        vote.setId(id);

        when(commentVoteRepository.findById(id)).thenReturn(java.util.Optional.of(vote));

        CommentVote result = commentVoteService.findById(id);

        assertEquals(vote, result);
    }

    @Test
    void testFindByUserAndComment() {
        User user = new User();
        Comment comment = new Comment();
        CommentVote commentVote = new CommentVote();

        when(commentVoteRepository.findByUserAndComment(user, comment)).thenReturn(java.util.Optional.of(commentVote));

        CommentVote result = commentVoteService.findByUserAndComment(user, comment);

        assertEquals(commentVote, result);
    }

    @Test
    void testSave() {
        CommentVote vote = new CommentVote();
        commentVoteService.save(vote);
        verify(commentVoteRepository).save(vote);
    }

    @Test
    void testVote() {
        CommentVoteRequest req = new CommentVoteRequest();
        req.setCommentId("commentId");
        req.setUserId("userId");
        User user = new User();
        user.setId(req.getUserId());
        user.setUsername("user");
        Comment comment = new Comment();
        comment.setId(req.getCommentId());
        CommentVote vote = new CommentVote();
        vote.setComment(comment);
        vote.setUser(user);
        vote.setVote(false);

        NotificationType type = new NotificationType();
        type.setName("commentLike");

        when(userService.findById(req.getUserId())).thenReturn(user);
        when(commentService.findById(req.getCommentId())).thenReturn(comment);
        when(commentVoteRepository.findByUserAndComment(user, comment)).thenReturn(java.util.Optional.empty());
        when(notificationTypeService.findByName("commentLike")).thenReturn(type);

        commentVoteService.vote(req);

        verify(commentVoteRepository).save(any(CommentVote.class));
        verify(notificationService).createNotification(eq("user voted on your comment"),
                eq("comment:" + req.getCommentId()),
                eq(comment.getUser()),
                any(NotificationType.class));
    }
}
