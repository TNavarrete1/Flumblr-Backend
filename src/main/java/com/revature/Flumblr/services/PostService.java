package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.PostRepository;
import java.util.List;
import java.util.ArrayList;
//import java.util.Optional;

import org.springframework.stereotype.Service;
import com.revature.Flumblr.dtos.responses.PostResponse;

import lombok.AllArgsConstructor;
import com.revature.Flumblr.entities.Post;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepo;

    public List<PostResponse> getUserPosts(String userId) {
        List<Post> userPosts = this.postRepo.getByUserIdOrderByCreateTimeDesc(userId);
        List<PostResponse> resPosts = new ArrayList<PostResponse>();
        for(Post userPost : userPosts) {
            resPosts.add(new PostResponse(userPost));
        }
        return resPosts;
    }
}
