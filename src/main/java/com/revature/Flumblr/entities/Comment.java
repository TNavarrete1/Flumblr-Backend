package com.revature.Flumblr.entities;

import java.util.UUID;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "comments")
public class Comment {
    @Id
    private String id;

    @Column(columnDefinition = "text")
    private String comment;

    @Column(nullable = false, name = "create_time")
    private Date createTime;

    @Column(nullable = false, name = "edit_time")
    private Date editTime;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(String comment, Post post, User user) {
        this.id = UUID.randomUUID().toString();
        this.comment = comment;
        this.createTime = new Date();
        this.editTime = this.createTime;
        this.post = post;
        this.user = user;
    }
}
