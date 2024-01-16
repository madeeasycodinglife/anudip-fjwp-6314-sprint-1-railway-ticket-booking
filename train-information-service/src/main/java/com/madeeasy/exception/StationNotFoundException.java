package com.madeeasy.exception;

import org.springframework.stereotype.Component;

@Component
public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException() {
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
