package com.madeeasy.exception.handler;

import com.madeeasy.exception.PlatformNotFoundException;
import com.madeeasy.exception.StationNotFoundException;
import com.madeeasy.exception.UpdateFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<?> handleStationNotFoundException(StationNotFoundException exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.NOT_FOUND);
        log.info("inside ProductNotFoundException handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // if you here put HttpStatus
    }

    @ExceptionHandler(UpdateFailedException.class)
    public ResponseEntity<?> handleException(UpdateFailedException exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.CONFLICT);
        log.info("inside UpdateFailedException handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse); // if you here put HttpStatus
    }

    @ExceptionHandler(PlatformNotFoundException.class)
    public ResponseEntity<?> handleException(PlatformNotFoundException exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.NOT_FOUND);
        log.info("inside PlatformNotFoundException handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // if you here put HttpStatus
    }

    //dataintegretyviolationexception handler
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleException(DataIntegrityViolationException exception) {
        Map<String, Object> errorResponse = Map.of(exception.getCause().toString(), HttpStatus.CONFLICT);
        log.info("inside DataIntegrityViolationException handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse); // if you here put HttpStatus
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.info("inside handleMethodArgumentNotValid handler: {}", ex.getMessage());
        BindingResult bindingResult = ex.getBindingResult();

        // Extract field-specific error messages
        Map<String, String> errors = bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));

        // Return a customized error response
        return ResponseEntity.badRequest().body(errors);
    }


    // global exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        Map<String, Object> errorResponse = Map.of(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.info("inside GlobalExceptionHandler handler: {}", errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // if you here put HttpStatus
    }
}
