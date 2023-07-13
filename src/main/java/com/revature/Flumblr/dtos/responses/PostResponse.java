package com.revature.Flumblr.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.revature.Flumblr.entities.Bookmark;
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.PostShare;
import com.revature.Flumblr.entities.PostVote;
import com.revature.Flumblr.entities.Tag;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse {
    private String id;

    private String username;
    private String userId;

    private String message;

    // media url
    private String s3Url;

    // url of profile image
    private String profileImg;

    private String mediaType;

    private int upVotes;

    private int downVotes;

    private PostVote userVote;

    private Bookmark bookmarked;

    private PostShare shared;

    private Date createTime;

    private Date editTime;

    private List<CommentResponse> comments;

    private Set<Tag> tags;

    private List<UserResponse> mentions;

    private int shareCount;

    private List<UserResponse> sharedBy;

    // have to get votes
    public PostResponse(Post post) {
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.userId = post.getUser().getId();
        this.message = post.getMessage();
        this.s3Url = post.getS3Url();
        this.profileImg = post.getUser().getProfile().getProfile_img();
        this.mediaType = post.getMediaType();
        this.createTime = post.getCreateTime();
        this.editTime = post.getEditTime();
        this.tags = post.getTags();
    }
}
