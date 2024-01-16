package com.madeeasy.exception;

import org.springframework.stereotype.Component;

@Component
public class TrainNotFoundException extends RuntimeException {
    public TrainNotFoundException() {
        super();
    }

    public TrainNotFoundException(String message) {
        super(message);
    }
}
