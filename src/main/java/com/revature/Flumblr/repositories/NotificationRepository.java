package com.revature.Flumblr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.Notification;
import com.revature.Flumblr.entities.User;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByUser(User user);

    List<Notification> findByUserOrderByCreateTimeDesc(User user);

}
