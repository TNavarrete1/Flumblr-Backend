package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.CommentRepository;
import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.PostVoteRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public Post findById(String postId) {
        Optional<Post> userPost = this.postRepository.findById(postId);
        if (userPost.isEmpty())
            throw new ResourceNotFoundException("Post(" + postId + ") Not Found");
        return userPost.get();
    }

    public List<PostResponse> getTrending(Date fromDate) {
        List<Post> responses = postRepository.findByCreateTimeGreaterThanEqual(fromDate);
        List<PostResponse> resPosts = new ArrayList<PostResponse>();

        class SortedPost {
            Post content;
            double score;
            SortedPost(Post content, double score) {
                this.content = content;
                this.score = score;
            }
        }

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
            double score = calculateScore(userPost);
            
            sortedPosts.add(new SortedPost(userPost, score));
        }

        while (!sortedPosts.isEmpty() && resPosts.size() <= 10) {
            resPosts.add(new PostResponse(sortedPosts.remove().content));
        }

        return resPosts;
    }

    private Double calculateScore(Post post) {
        // if performance is an issue these can probably be sql 'count' queries
         List<PostVote> listOfVotes = postVoteRepository.findAllByPost(post);
         Integer numberofComments = commentRepository.findAllByPost(post).size();
        Double score = 0.0;
        score = (numberofComments * 2) + score;
        for (PostVote vote: listOfVotes) {
            if (vote.isVote()) {
                score = score + 1.5;
            }
            else {
                score = score - 1;
            }
        }
        return score;
    }
}
