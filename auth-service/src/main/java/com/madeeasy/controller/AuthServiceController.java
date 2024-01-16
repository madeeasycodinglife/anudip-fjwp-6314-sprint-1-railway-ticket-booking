package com.madeeasy.controller;

import com.madeeasy.dto.response.AuthenticationResponse;
import com.madeeasy.service.AuthenticationService;
import com.madeeasy.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth-service")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthServiceController {

    private final AuthenticationService authenticationService;
    private final JwtUtils jwtUtils;

    @Operation(
            summary = "Authenticate to get access JWT token",
            description = "Authenticate to get access JWT token",
            tags = {"Authenticate to get access JWT Token"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success Authentication",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AuthenticationResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Failed Authentication",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Failed Authentication",
                            content = @Content
                    )
            }
    )
    @PostMapping("/authenticate/{email}/{password}")
    public ResponseEntity<?> authenticate(@PathVariable("email") String email,
                                          @PathVariable("password") String password) {
        return this.authenticationService.generateAccessToken(email, password);
    }

    @Operation(
            summary = "Protected Only for Authenticated Admins",
            description = "Protected Only for Authenticated Admins",
            tags = {"Protected Only for Authenticated Admins"}
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/protected-api/only-for-admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> protectedApiOnlyForAuthenticatedAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hi " + authentication.getPrincipal() + " welcome to protected admin api");
    }

    @Operation(
            summary = "Protected Only for Authenticated Users",
            description = "Protected Only for Authenticated Users",
            tags = {"Protected Only for Authenticated Users"}
    )

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/protected-api/only-for-authenticated-users")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> protectedApiOnlyForAuthenticatedUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hi " + authentication.getPrincipal() + " welcome to protected user api");
    }

    @Operation(
            summary = "Protected Only for Authenticated Admins and Users",
            description = "Protected Only for Authenticated Admins and Users",
            tags = {"Protected Only for Authenticated Admins and Users"}
    )

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/protected-api/only-for-authenticated-admins-and-users")
    @PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> protectedApiOnlyForAuthenticatedAdminsAndUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hi " + authentication.getPrincipal() + " welcome to protected admin and  user api");
    }


}
