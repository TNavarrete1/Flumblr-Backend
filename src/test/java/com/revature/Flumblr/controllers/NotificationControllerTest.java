package com.revature.Flumblr.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.Flumblr.dtos.requests.NotificationRequest;
import com.revature.Flumblr.dtos.responses.NotificationResponse;
import com.revature.Flumblr.entities.Notification;
import com.revature.Flumblr.services.NotificationService;
import com.revature.Flumblr.services.TokenService;

public class NotificationControllerTest {
    @Mock
    TokenService tokenService;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteNotification() {
        String token = "valid";
        String notificationId = "id";

        when(tokenService.extractUserId(token)).thenReturn("user");

        ResponseEntity<?> response = notificationController.deleteNotification(token, notificationId);

        verify(notificationService).deleteNotification(notificationId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetAllNotification() {
        String token = "valid";
        String userId = "id";

        List<NotificationResponse> notifications = new ArrayList<>();

        when(tokenService.extractUserId(token)).thenReturn(userId);
        when(notificationService.getNotificationByUser(userId)).thenReturn(notifications);

        ResponseEntity<List<NotificationResponse>> response = notificationController.getAllNotification(token, userId);

        assertEquals(notifications, response.getBody());
    }

    @Test
    void testGetNotification() {
        String token = "valid";
        String notificationId = "id";

        Notification notification = new Notification();
        notification.setId(notificationId);

        when(tokenService.extractUserId(token)).thenReturn("user");
        when(notificationService.findById(notificationId)).thenReturn(notification);

        ResponseEntity<Notification> response = notificationController.getNotification(token, notificationId);

        assertEquals(notification, response.getBody());
    }

    @Test
    void testGetUnreadNotification() {
        String token = "valid";
        String userId = "id";

        List<NotificationResponse> notifications = new ArrayList<>();

        when(tokenService.extractUserId(token)).thenReturn(userId);
        when(notificationService.getUnreadNotificationByUser(userId)).thenReturn(notifications);

        ResponseEntity<List<NotificationResponse>> response = notificationController.getUnreadNotification(token,
                userId);

        assertEquals(notifications, response.getBody());
    }

    @Test
    void testReadNotification() {
        NotificationRequest req = new NotificationRequest();
        req.setNotificationId("id");
        req.setUserId("user");
        String token = "valid";

        notificationController.readNotification(req, token);

        verify(notificationService).readNotification(req);

    }
}
