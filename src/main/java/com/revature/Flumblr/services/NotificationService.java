package com.revature.Flumblr.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.dtos.requests.NotificationRequest;
import com.revature.Flumblr.entities.Notification;
import com.revature.Flumblr.entities.NotificationType;
import com.revature.Flumblr.entities.User;
import com.revature.Flumblr.repositories.NotificationRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public void createNotification(String message, String link, User user, NotificationType notificationType) {
        Notification notification = new Notification(message, user, link, notificationType);
        notificationRepository.save(notification);
    }

    public void deleteNotification(String id) {
        Notification notification = findById(id);
        notificationRepository.delete(notification);
    }

    public void readNotification(NotificationRequest req) {
        Notification notification = findById(req.getNotificationId());
        notification.setViewed(true);
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationByUser(String userId) {
        User user = userService.findById(userId);
        List<Notification> notifications = notificationRepository.findByUser(user);
        return notifications;
    }

    public Notification findById(String id) {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);

        if (notificationOpt.isEmpty()) {
            throw new ResourceNotFoundException("Notification " + id + " not found");
        }
        return notificationOpt.get();
    }

}
