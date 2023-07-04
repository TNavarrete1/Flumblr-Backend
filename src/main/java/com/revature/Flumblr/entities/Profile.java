package com.revature.Flumblr.entities;

import java.sql.Types;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    private String id;

    //@Lob
    @Column
    @JdbcTypeCode(Types.VARBINARY)
    //DB script should use profileImg=bytea as type in postgres
    private byte[] profile_img;

    @Column
    private String bio;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(name = "profile_tag_list", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    public Profile(User user, byte[] profile_img, String bio) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.profile_img = profile_img;
        this.bio = bio;
    }

}
