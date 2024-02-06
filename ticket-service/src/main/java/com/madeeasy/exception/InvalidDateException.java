package com.madeeasy.exception;

import org.springframework.stereotype.Component;

@Component
public class InvalidDateException extends RuntimeException {

    public InvalidDateException() {
        super();
    }

    public InvalidDateException(String message) {
        super(message);
    }
}
