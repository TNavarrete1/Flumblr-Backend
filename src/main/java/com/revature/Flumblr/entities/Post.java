package com.revature.Flumblr.entities;

import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

    @Id
    private String id;

    @Column(columnDefinition = "text")
    private String message;

    @Column
    private String s3bucket;

    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "edit_time")
    private Date editTime;

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    Post(String message, String mediaType, String s3bucket) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.mediaType = mediaType;
        this.s3bucket = s3bucket;
        this.createTime = new Date();
        this.editTime = this.createTime;
        this.comments = new ArrayList<Comment>();
    }
}
