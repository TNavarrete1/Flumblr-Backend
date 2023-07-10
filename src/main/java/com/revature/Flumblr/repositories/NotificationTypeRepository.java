package com.revature.Flumblr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.Flumblr.entities.NotificationType;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, String> {

    Optional<NotificationType> findByNameIgnoreCase(String name);

}
