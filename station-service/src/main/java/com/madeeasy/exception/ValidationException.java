package com.madeeasy.exception;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
