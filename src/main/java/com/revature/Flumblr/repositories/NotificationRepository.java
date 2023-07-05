package com.revature.Flumblr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, String> {

}
