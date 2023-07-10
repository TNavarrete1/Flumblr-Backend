package com.revature.Flumblr.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.PostShare;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.PostShareRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostShareService {

    private final PostShareRepository postShareRepository;
    private final UserService userService;
    private final PostService postService;
    private final NotificationService notificationService;
    private final NotificationTypeService notificationTypeService;

    public void create(String postId, String userId) {
        Optional<PostShare> postShareOpt = postShareRepository.findByPostIdAndUserId(postId, userId);
        if(!postShareOpt.isEmpty()) {
            throw new ResourceConflictException("already shared Post(" + postId + ')');
        }
        User user = userService.findById(userId);
        Post post = postService.findById(postId);
        if(post.getUser().getId().equals(user.getId()))
            throw new ResourceConflictException("Post(" + postId + ") is by sharing User(" + userId + ')');
        PostShare postShare = new PostShare(user, post);
        postShareRepository.save(postShare);
        notificationService.createNotification("User " + user.getUsername() + " shared your post",
                "post:" + post.getId(), post.getUser(), notificationTypeService.findByName("share"));
    }

    @Transactional
    public void delete(String postId, String userId) {
        Optional<PostShare> postShareOpt = postShareRepository.findByPostIdAndUserId(postId, userId);
        if(postShareOpt.isEmpty()) {
            throw new ResourceConflictException("haven't shared Post(" + postId + ')');
        }
        postShareRepository.delete(postShareOpt.get());
    }
}
