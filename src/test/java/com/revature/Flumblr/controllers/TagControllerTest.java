package com.revature.Flumblr.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.Flumblr.services.*;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    private TagController tagController;

    @Mock
    private TokenService tokenService;

    @Mock
    private TagService tagService;

    @BeforeEach
    public void setup() {
        tagController = new TagController(tokenService, tagService);
    }

    @Test
    public void getTrendingTest() {
        List<String> tagNames = Arrays.asList("blue", "cat", "summer");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date inDate = null;
        try {
            inDate = formatter.parse("29-06-2023");
        } catch (Exception e) {
            fail();
        }
        when(tagService.getTrending(inDate)).thenReturn(tagNames);
        ResponseEntity<List<String>> resTags = tagController.getTrending(inDate, "dummyToken");
        verify(tokenService, times(1)).extractUserId("dummyToken");
        verify(tagService, times(1)).getTrending(inDate);
        assertEquals(resTags.getBody(), tagNames);
        assertEquals(resTags.getStatusCode(), HttpStatus.OK);
    }
}
