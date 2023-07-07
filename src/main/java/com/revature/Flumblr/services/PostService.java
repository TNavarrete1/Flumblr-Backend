package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_classes.SortedPost;
import com.revature.Flumblr.utils.custom_exceptions.FileNotUploadedException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
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

    public List<Post> getFollowing(String userId, int page) {
        User user = userService.findById(userId);
        List<User> following = new ArrayList<User>();
        for (Follow follow : user.getFollows()) {
            following.add(follow.getFollow());
        }
        return postRepository.findAllByUserIn(following,
                PageRequest.of(page, 20, Sort.by("createTime").descending()));
    }

    public List<Post> getFeed(int page) {
        return postRepository.findAllBy(PageRequest.of(page, 20, Sort.by("createTime").descending()));
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

    public List<PostResponse> getTrending(Date fromDate) {
        List<Post> responses = postRepository.findByCreateTimeGreaterThanEqual(fromDate);

        PriorityQueue<SortedPost> sortedPosts = new PriorityQueue<SortedPost>(11,
        new Comparator<SortedPost>() {
            @Override
            public int compare(SortedPost post1, SortedPost post2) {
                return Double.compare(post1.getScore(), post2.getScore());
            }
        });

        for (Post userPost : responses) {
            // Integer numberOfVotes = postVoteRepository.findAllByPost(userPost).size();
            SortedPost sortedPost = new SortedPost(userPost);
            calculateScore(sortedPost);
            
            sortedPosts.add(sortedPost);
            if(sortedPosts.size() > 10) sortedPosts.poll();
        }

        PostResponse[] resPosts = new PostResponse[sortedPosts.size()];
        for (int i = sortedPosts.size() - 1; i >= 0; i--) {
            SortedPost sortedPost = sortedPosts.poll();
            resPosts[i] = new PostResponse(sortedPost.getContent(), sortedPost.getUpvotes(),
                sortedPost.getDownvotes());
        }

        return Arrays.asList(resPosts);
    }

    private void calculateScore(SortedPost sortedPost) {
        Post post = sortedPost.getContent();
        Set<PostVote> postVotes = post.getPostVotes();
        int upVotes = 0;
        for(PostVote postVote : postVotes) {
            if(postVote.isVote()) upVotes++;
        }
        int downVotes = postVotes.size() - upVotes;
        int numberofComments = post.getComments().size();
        double score = downVotes + upVotes*1.5;
        score += (numberofComments * 2);
        sortedPost.setScore(score);
        sortedPost.setUpvotes(upVotes);
        sortedPost.setDownvotes(downVotes);
    }

}
