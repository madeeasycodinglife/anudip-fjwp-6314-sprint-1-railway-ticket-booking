package com.madeeasy.exception;

import org.springframework.stereotype.Component;

@Component
public class PlatformNotFoundException extends RuntimeException {
    public PlatformNotFoundException() {
        super();
    }

    public PlatformNotFoundException(String message) {
        super(message);
    }
}
