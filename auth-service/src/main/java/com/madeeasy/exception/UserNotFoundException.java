package com.madeeasy.exception;

import org.springframework.stereotype.Component;

@Component
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
