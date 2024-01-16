package com.madeeasy.exception;

import org.springframework.stereotype.Component;

@Component
public class UpdateFailedException extends RuntimeException {

    public UpdateFailedException() {
    }

    public UpdateFailedException(String message) {
        super(message);
    }
}
