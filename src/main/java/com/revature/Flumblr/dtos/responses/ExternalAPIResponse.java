package com.revature.Flumblr.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExternalAPIResponse {

    String result;
    String reason;
    String disposable;
    String did_you_mean;
    String domain;
    String success;
    
}
