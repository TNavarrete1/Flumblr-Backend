package com.revature.Flumblr.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

    @Test
    void testCreateNotification() {
        String message = "hi";
        String link = "link";
        User user = new User();
        NotificationType type = new NotificationType();

        Notification notification = new Notification(message, user, link, type);

        verify(notificationRepository).save(notification);
    }

    @Test
    void testDeleteNotification() {

    }

    @Test
    void testFindById() {

    }

    @Test
    void testGetNotificationByUser() {

    }

    @Test
    void testGetUnreadNotificationByUser() {

    }

    @Test
    void testReadNotification() {

    }
}
