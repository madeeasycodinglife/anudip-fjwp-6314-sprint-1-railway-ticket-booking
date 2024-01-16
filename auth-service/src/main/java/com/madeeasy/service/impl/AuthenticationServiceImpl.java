package com.madeeasy.service.impl;

import com.madeeasy.dto.response.AuthenticationResponse;
import com.madeeasy.service.AuthenticationService;
import com.madeeasy.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtils jwtUtils;

    @Override
    public boolean validateUserName(String userName) {

        return false;
    }

    @Override
    public ResponseEntity<?> generateAccessToken(String email, String password) {
        String accessToken = this.jwtUtils.generateToken(email, password);
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
