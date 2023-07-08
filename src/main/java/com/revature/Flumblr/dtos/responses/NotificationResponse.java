package com.revature.Flumblr.dtos.responses;

import java.util.Date;

import com.revature.Flumblr.entities.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationResponse {
    private String id;

    private String message;

    private boolean viewed;

    private String link;

    private Date createTime;

    private String username;

    private String notificationType;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.username = notification.getUser().getUsername();
        this.viewed = notification.isViewed();
        this.link = notification.getLink();
        this.notificationType = notification.getNotificationType().getName();
        this.createTime = notification.getCreateTime();
    }
}
