package com.revature.Flumblr.services;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.NotificationType;
import com.revature.Flumblr.repositories.NotificationTypeRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class NotificationTypeService {
    NotificationTypeRepository notificationTypeRepository;

    public NotificationType findByName(String name) {
        return notificationTypeRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Ability (" + name + ") not found!"));
    }
}
