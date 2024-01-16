package com.madeeasy.exception;

import org.springframework.stereotype.Component;

@Component
public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String message) {
        super(message);
    }

    public TicketNotFoundException() {
    }
}
