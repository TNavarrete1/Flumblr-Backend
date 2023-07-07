package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_exceptions.FileNotUploadedException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Date;
import java.util.ArrayList;
import java.util.Comparator;

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
    private final UserService userService;
    private final UserRepository userRepository;
    private final S3StorageService s3StorageService;

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
            Optional<Post> postOptional = postRepository.findById(postId);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                s3StorageService.deleteFileFromS3Bucket(post.getS3Url());

                postRepository.deleteById(postId);
            } else {
                throw new ResourceNotFoundException("Post with id " + postId + " was not found");
            }

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

    public PostResponse updatePost(String postId, MultipartHttpServletRequest req, String fileUrl) {
        Post post = this.findById(postId);
        String newMessage = req.getParameter("message");
        String newMediaType = req.getParameter("mediaType");
        String existingFileUrl = post.getS3Url();

        if (existingFileUrl != null && !existingFileUrl.isEmpty()) {
            s3StorageService.deleteFileFromS3Bucket(existingFileUrl);
        }
        if (newMessage != null && !newMessage.isEmpty()) {
            post.setMessage(newMessage);
        }

        if (newMediaType != null && !newMediaType.isEmpty()) {
            post.setMediaType(newMediaType);
        }

        if (fileUrl != null && !fileUrl.isEmpty()) {
            post.setS3Url(fileUrl);
        }
        post.setEditTime(new Date());
        postRepository.save(post);
        PostResponse response = new PostResponse(post);
        return response;
    }

    class SortedPost {
        Post content;
        double score;
        long upVotes;
        long downVotes;
        SortedPost(Post content) {
            this.content = content;
        }
    }

    public List<PostResponse> getTrending(Date fromDate) {
        List<Post> responses = postRepository.findByCreateTimeGreaterThanEqual(fromDate);
        List<PostResponse> resPosts = new ArrayList<PostResponse>();


        PriorityQueue<SortedPost> sortedPosts = new PriorityQueue<SortedPost>(responses.size(),
        new Comparator<SortedPost>() {
            @Override
            public int compare(SortedPost post1, SortedPost post2) {
                double score1 = post1.score;
                double score2 = post2.score;

                if (score1 < score2) {
                    return 1; // Return a positive value to indicate post2 should come before post1
                } else if (score1 > score2) {
                    return -1; // Return a negative value to indicate post1 should come before post2
                } else {
                    return 0; // Return 0 to indicate the scores are equal
                }
            }
        });

        for (Post userPost : responses) {
            // Integer numberOfVotes = postVoteRepository.findAllByPost(userPost).size();
            SortedPost sortedPost = new SortedPost(userPost);
            calculateScore(sortedPost);
            
            sortedPosts.add(sortedPost);
        }

        while (!sortedPosts.isEmpty() && resPosts.size() <= 10) {
            SortedPost sortedPost = sortedPosts.remove();
            resPosts.add(new PostResponse(sortedPost.content, sortedPost.upVotes, sortedPost.downVotes));
        }

        return resPosts;
    }

    private void calculateScore(SortedPost sortedPost) {
        Post post = sortedPost.content;
        Set<PostVote> postVotes = post.getPostVotes();
        long upVotes = 0;
        for(PostVote postVote : postVotes) {
            if(postVote.isVote()) upVotes++;
        }
        long downVotes = postVotes.size() - upVotes;
        long numberofComments = post.getComments().size();
        double score = -downVotes;
        score += upVotes*1.5;
        score += (numberofComments * 2);
        sortedPost.score = score;
        sortedPost.upVotes = upVotes;
        sortedPost.downVotes = downVotes;
    }

}
