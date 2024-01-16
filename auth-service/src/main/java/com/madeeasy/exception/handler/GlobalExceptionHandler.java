package com.madeeasy.exception.handler;

import com.madeeasy.exception.TokenException;
import com.madeeasy.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.NOT_FOUND);
        log.info("inside ProductNotFoundException handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // if you here put HttpStatus
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> handleException(TokenException exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        log.info("inside TokenException handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse); // if you here put HttpStatus
    }

}
