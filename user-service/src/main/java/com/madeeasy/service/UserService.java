package com.madeeasy.service;

import com.madeeasy.dto.request.UserRequestDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createUser(UserRequestDTO userRequestDTO);
    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getUserByEmail(String email);
}
