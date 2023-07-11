package com.revature.Flumblr.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.Flumblr.services.*;
import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.dtos.requests.ReportPostRequest;
import com.revature.Flumblr.dtos.responses.ReportResponse;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;
import com.revature.Flumblr.utils.custom_exceptions.UnauthorizedException;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    private TagController tagController;
}
