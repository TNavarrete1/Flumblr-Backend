package com.revature.Flumblr.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

import com.revature.Flumblr.entities.Report;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportResponse {
    private String id;

    private Date createTime;

    private String reason;

    private String userId;

    private String postId;
    
    public ReportResponse(Report report) {
        this.id = report.getId();
        this.createTime = report.getCreateTime();
        this.reason = report.getReason();
        this.userId = report.getUser().getId();
        this.postId = report.getPost().getId();
    }
}
