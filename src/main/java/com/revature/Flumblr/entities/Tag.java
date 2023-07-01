package com.revature.Flumblr.entities;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "tags")
public class Tag {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Post> posts;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Profile> profiles;

    public Tag(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

}
