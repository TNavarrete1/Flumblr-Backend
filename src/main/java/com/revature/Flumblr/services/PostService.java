package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.BookmarksRepository;
import com.revature.Flumblr.repositories.CommentRepository;
import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_exceptions.FileNotUploadedException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;
import com.revature.Flumblr.repositories.PostVoteRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.revature.Flumblr.dtos.responses.PostResponse;

import com.revature.Flumblr.entities.User;

import lombok.AllArgsConstructor;

import com.revature.Flumblr.entities.Follow;
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.PostVote;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BookmarksRepository bookmarksRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PostVoteRepository postVoteRepository;
    private final CommentRepository commentRepository;

    public List<Post> getFeed(String userId, int page) {
        User user = userService.findById(userId);
        List<User> following = new ArrayList<User>();
        for (Follow follow : user.getFollows()) {
            following.add(follow.getFollow());
        }
        return postRepository.findAllByUserIn(following,
                PageRequest.of(page, 20, Sort.by("createTime").descending()));
    }

    public List<Post> findByTag(List<String> tags, int page) {
        return postRepository.findAllByTagsNameIn(tags,
                PageRequest.of(page, 20, Sort.by("createTime").descending()));
    }

    public List<Post> getUserPosts(String userId) {
        return this.postRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public Post getPost(String postId) {
        Optional<Post> userPost = this.postRepository.findById(postId);
        if (userPost.isEmpty())
            throw new ResourceNotFoundException("Post(" + postId + ") Not Found");
        return userPost.get();
    }

    public Post findById(String postId) {
        Optional<Post> userPost = this.postRepository.findById(postId);
        if (userPost.isEmpty())
            throw new ResourceNotFoundException("Post(" + postId + ") Not Found");
        return userPost.get();
    }

    public String getPostOwner(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " was not found"));
        return post.getUser().getId();
    }

    public void deletePost(String postId) {
        try {
            postRepository.deleteById(postId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Post with id " + postId + " was not found");
        }
    }

    public void createPost(MultipartHttpServletRequest req, String fileUrl, String userId) {

        Optional<User> userOpt = userRepository.findById(userId);

        User user = userOpt.get();

        String message = req.getParameter("message");

        if (message == null && fileUrl == null) {
            throw new ResourceConflictException("Message or media required!");
        }

        String mediaType = req.getParameter("mediaType");
        if (mediaType == null) {
            throw new FileNotUploadedException("Media Type can not be empty!");
        }

        Post post = new Post(message, mediaType, fileUrl, user);

        postRepository.save(post);

    }

    public List<PostResponse> getTrending(Date fromDate) {
        List<Post> responses = postRepository.findAll();
        List<PostResponse> resPosts = new ArrayList<PostResponse>();

        for (Post userPost : responses) {
            // Integer numberOfVotes = postVoteRepository.findAllByPost(userPost).size();
            Double score = CalculateScore(userPost);
            int checkWhen = fromDate.compareTo(userPost.getCreateTime());

            if (checkWhen == -1 || checkWhen == 0) {
                resPosts.add(new PostResponse(userPost, score));
            }
        }

        Collections.sort(resPosts, new Comparator<PostResponse>() {
            @Override
            public int compare(PostResponse post1, PostResponse post2) {
                double score1 = post1.getScore();
                double score2 = post2.getScore();

                if (score1 < score2) {
                    return 1; // Return a positive value to indicate post2 should come before post1
                } else if (score1 > score2) {
                    return -1; // Return a negative value to indicate post1 should come before post2
                } else {
                    return 0; // Return 0 to indicate the scores are equal
                }
            }
        });

        List<PostResponse> limitedList = resPosts.subList(0, Math.min(resPosts.size(), 10));
        return limitedList;
    }

    private Double CalculateScore(Post post) {
        List<PostVote> listOfVotes = postVoteRepository.findAllByPost(post);
        Integer numberofComments = commentRepository.findAllByPost(post).size();
        Double score = 0.0;
        score = (numberofComments * 2) + score;
        for (PostVote vote : listOfVotes) {
            if (vote.isVote()) {
                score = score + 1.5;
            } else {
                score = score - 1;
            }
        }
        return score;
    }
}
