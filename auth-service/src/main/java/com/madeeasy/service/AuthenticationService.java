package com.madeeasy.service;

import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    boolean validateUserName(String userName);

    ResponseEntity<?> generateAccessToken(String email, String password);
}
