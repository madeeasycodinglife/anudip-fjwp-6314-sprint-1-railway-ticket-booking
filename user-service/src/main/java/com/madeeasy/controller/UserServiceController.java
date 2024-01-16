package com.madeeasy.controller;

import com.madeeasy.dto.request.UserRequestDTO;
import com.madeeasy.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user-service")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserServiceController {

    private final UserService userService;

    @Operation(
            summary = "Create a New User",
            description = "Creating a New User",
            tags = {"Create a New User"}
    )
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        return this.userService.createUser(userRequestDTO);
    }


    @Operation(
            summary = "Get all Users",
            description = "Get all Users",
            tags = {"Get all Users"}
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/get-all-users")
    public ResponseEntity<?> getAllUsers() {
        return this.userService.getAllUsers();
    }


    @Operation(
            summary = "Get user by email",
            description = "Get user by email",
            tags = {"Get User By Email"}
    )
    @GetMapping(value = "/get-user-by-email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        return this.userService.getUserByEmail(email);
    }

    @PutMapping(value = "/update/password/{email}")
    @Hidden
    public ResponseEntity<?> updatePassword(@PathVariable String email) {
        return null;
    }

    @PutMapping(value = "/forgot/password")
    @Hidden
    public ResponseEntity<?> forgotPassword() {
        return null;
    }
}
