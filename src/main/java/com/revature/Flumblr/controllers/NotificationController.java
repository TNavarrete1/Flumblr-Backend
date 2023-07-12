package com.revature.Flumblr.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.Flumblr.dtos.requests.NotificationRequest;
import com.revature.Flumblr.dtos.responses.NotificationResponse;
import com.revature.Flumblr.entities.Notification;
import com.revature.Flumblr.services.NotificationService;
import com.revature.Flumblr.services.TokenService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final TokenService tokenService;
    private final NotificationService notificationService;

    @GetMapping("/{notificationId}")
    public ResponseEntity<Notification> getNotification(@RequestHeader("Authorization") String token,
            @PathVariable String notificationId) {
        tokenService.extractUserId(token);

        return ResponseEntity.status(HttpStatus.OK).body(notificationService.findById(notificationId));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<NotificationResponse>> getAllNotification(@RequestHeader("Authorization") String token,
            @PathVariable String userId) {
        tokenService.extractUserId(token);

        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationByUser(userId));
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotification(
            @RequestHeader("Authorization") String token,
            @PathVariable String userId) {
        tokenService.extractUserId(token);

        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getUnreadNotificationByUser(userId));
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<?> readNotification(@RequestBody NotificationRequest req,
            @RequestHeader("Authorization") String token) {

        tokenService.validateToken(token, req.getUserId());
        notificationService.readNotification(req);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotification(@RequestHeader("Authorization") String token,
            @PathVariable String notificationId) {
        tokenService.extractUserId(token);
        notificationService.deleteNotification(notificationId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
