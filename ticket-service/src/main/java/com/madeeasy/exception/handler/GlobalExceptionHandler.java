package com.madeeasy.exception.handler;

import com.madeeasy.exception.TicketNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Validation failed");
        errorResponse.put("errors", extractFieldErrors(ex));

        log.error("Validation failed: {}", errorResponse);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    private Map<String, String> extractFieldErrors(ConstraintViolationException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                fieldErrors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return fieldErrors;
    }

    // TicketNotFoundException handler

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<?> handleException(TicketNotFoundException exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.NOT_FOUND);
        log.info("inside TicketNotFoundException handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // if you here put HttpStatus
    }

    // global exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.info("inside GlobalExceptionHandler handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // if you here put HttpStatus
    }
}
