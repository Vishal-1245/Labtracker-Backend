package com.college.labtracker.controller;

import java.security.Principal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.college.labtracker.dto.auth.ChangePasswordRequest;
import com.college.labtracker.dto.auth.LoginRequest;
import com.college.labtracker.dto.auth.LoginResponse;
import com.college.labtracker.service.AuthService;
import jakarta.validation.Valid;

// Controller for authentication and password lifecycle operations
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/login: Authenticates user and returns JWT token
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // POST /api/auth/change-password: Allows authenticated student or staff to update password
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> changePassword(
            Principal principal,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        Map<String, String> response = authService.changePassword(principal.getName(), request);
        return ResponseEntity.ok(response);
    }
}
