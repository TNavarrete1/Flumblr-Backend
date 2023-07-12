package com.revature.Flumblr.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.Flumblr.dtos.requests.NotificationRequest;
import com.revature.Flumblr.dtos.responses.NotificationResponse;
import com.revature.Flumblr.entities.Notification;
import com.revature.Flumblr.entities.NotificationType;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.NotificationRepository;

public class NotificationServiceTest {
    @Mock
    NotificationRepository notificationRepository;

    @Mock
    UserService userService;

    @InjectMocks
    NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNotification() {
        String message = "hi";
        String link = "link";
        User user = new User();
        NotificationType type = new NotificationType();

        notificationService.createNotification(message, link, user, type);

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testDeleteNotification() {
        String id = "id";
        Notification notification = new Notification();
        notification.setId(id);

        when(notificationRepository.findById(id)).thenReturn(java.util.Optional.of(notification));

        notificationService.deleteNotification(id);

        verify(notificationRepository).delete(notification);
    }

    @Test
    void testFindById() {
        String id = "id";
        Notification notification = new Notification();
        notification.setId(id);

        when(notificationRepository.findById(id)).thenReturn(java.util.Optional.of(notification));

        Notification result = notificationService.findById(id);

        assertEquals(notification, result);

    }

    @Test
    void testGetNotificationByUser() {
        String userId = "user";
        User user = new User();
        user.setId(userId);

        List<Notification> notifications = new ArrayList<>();
        List<NotificationResponse> responses = new ArrayList<>();

        when(notificationRepository.findByUserOrderByCreateTimeDesc(user)).thenReturn(notifications);

        List<NotificationResponse> results = notificationService.getNotificationByUser(userId);

        assertEquals(responses, results);

    }

    @Test
    void testGetUnreadNotificationByUser() {
        String userId = "user";
        User user = new User();
        user.setId(userId);

        List<Notification> notifications = new ArrayList<>();
        List<NotificationResponse> responses = new ArrayList<>();

        when(notificationRepository.findByUserAndViewedOrderByCreateTimeDesc(user, false))
                .thenReturn(notifications);

        List<NotificationResponse> results = notificationService.getNotificationByUser(userId);

        assertEquals(responses, results);
    }

    @Test
    void testReadNotification() {
        NotificationRequest req = new NotificationRequest();
        req.setNotificationId("notificationId");
        req.setUserId("userId");
        String id = "id";
        Notification notification = new Notification();
        notification.setId(id);

        when(notificationRepository.findById(req.getNotificationId())).thenReturn(java.util.Optional.of(notification));

        notificationService.readNotification(req);

        verify(notificationRepository).save(notification);
    }
}
