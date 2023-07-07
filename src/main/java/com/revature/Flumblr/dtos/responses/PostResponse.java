package com.revature.Flumblr.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.PostVote;
import com.revature.Flumblr.entities.Tag;
import com.revature.Flumblr.entities.Comment;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse {
    private String id;

    private String username;

    private String message;

    // media url
    private String s3Url;

    // url of profile image
    private String profileImg;

    private String mediaType;

    private int upVotes;

    private int downVotes;

    private Date createTime;

    private Date editTime;

    private List<CommentResponse> comments;

    private Set<Tag> tags;

    public PostResponse(Post post, int upVotes, int downVotes) {
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.message = post.getMessage();
        this.s3Url = post.getS3Url();
        this.profileImg = post.getUser().getProfile().getProfile_img();
        this.mediaType = post.getMediaType();
        this.createTime = post.getCreateTime();
        this.editTime = post.getEditTime();
        this.comments = new ArrayList<CommentResponse>();
        this.tags = post.getTags();
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        for (Comment comment : post.getComments()) {
            this.comments.add(new CommentResponse(comment));
        }
    }

    // have to get votes
    public PostResponse(Post post) {
        Set<PostVote> postVotes = post.getPostVotes();
        int upVotes = 0;
        for(PostVote postVote : postVotes) {
            if(postVote.isVote()) upVotes++;
        }
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.message = post.getMessage();
        this.s3Url = post.getS3Url();
        this.profileImg = post.getUser().getProfile().getProfile_img();
        this.mediaType = post.getMediaType();
        this.createTime = post.getCreateTime();
        this.editTime = post.getEditTime();
        this.comments = new ArrayList<CommentResponse>();
        this.tags = post.getTags();
        this.upVotes = upVotes;
        this.downVotes = postVotes.size() - upVotes;
        for (Comment comment : post.getComments()) {
            this.comments.add(new CommentResponse(comment));
        }
    }
}
