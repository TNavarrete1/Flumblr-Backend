package com.revature.Flumblr.dtos.responses;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PotentialFollowerResponse {
    private String profile_img;
    private String bio;
    private String username;
    private String profileId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PotentialFollowerResponse other = (PotentialFollowerResponse) obj;
        return Objects.equals(profile_img, other.profile_img)
                && Objects.equals(bio, other.bio)
                && Objects.equals(username, other.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile_img, bio, username);
    }
}