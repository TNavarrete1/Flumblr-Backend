package com.revature.Flumblr.controllers;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.revature.Flumblr.utils.custom_exceptions.InvalidTokenException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;
import com.revature.Flumblr.utils.custom_exceptions.UnauthorizedException;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;

import java.util.Map;
import java.util.Date;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date(System.currentTimeMillis()));
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date(System.currentTimeMillis()));
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Map<String, Object>> handleResourceConflictException(ResourceConflictException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date(System.currentTimeMillis()));
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date(System.currentTimeMillis()));
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTokenException(InvalidTokenException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date(System.currentTimeMillis()));
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
}
