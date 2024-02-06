package com.madeeasy.exception.handler;

import com.madeeasy.exception.TokenException;
import com.madeeasy.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", "User Not Found");
        errorResponse.put("message", exception.getMessage());
        log.info("inside UserNotFoundException handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // if you here put HttpStatus
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> handleException(TokenException exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        log.info("inside TokenException handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse); // if you here put HttpStatus
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.FORBIDDEN.value());
        errorResponse.put("error", "Access Denied");
        errorResponse.put("message", "You are not authorized to access this resource.");
        errorResponse.put("details", exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.info("inside GlobalExceptionHandler handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // if you here put HttpStatus
    }
}
