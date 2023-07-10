package com.revature.Flumblr.dtos.responses;

import java.util.List;
import com.revature.Flumblr.entities.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TagInterestResponse {

    private List<Tag> tags;

}
