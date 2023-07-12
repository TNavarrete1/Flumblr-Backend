package com.revature.Flumblr.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.Flumblr.entities.NotificationType;
import com.revature.Flumblr.repositories.NotificationTypeRepository;

public class NotificationTypeServiceTest {
    @Mock
    NotificationTypeRepository notificationTypeRepository;

    @InjectMocks
    NotificationTypeService notificationTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByName() {
        String name = "post";

        NotificationType type = new NotificationType();
        type.setName(name);

        when(notificationTypeRepository.findByNameIgnoreCase(name)).thenReturn(java.util.Optional.of(type));

        NotificationType result = notificationTypeService.findByName(name);

        assertEquals(type, result);

    }
}
