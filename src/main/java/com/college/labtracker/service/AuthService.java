package com.college.labtracker.service;

import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.college.labtracker.dto.auth.ChangePasswordRequest;
import com.college.labtracker.dto.auth.LoginRequest;
import com.college.labtracker.dto.auth.LoginResponse;
import com.college.labtracker.entity.User;
import com.college.labtracker.repository.UserRepository;
import com.college.labtracker.security.JwtService;

// Service handling authentication business logic
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // Authenticates user and returns JWT token with profile details and mustChangePassword flag
    public LoginResponse login(LoginRequest request) {
        // Authenticate credentials against CustomUserDetailsService & BCrypt
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Fetch user record from database
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));

        if (!user.isActive()) {
            throw new DisabledException("Account is deactivated");
        }

        // Generate JWT token containing username, role, and fullName claims
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name(), user.getFullName());

        return new LoginResponse(token, user.getRole().name(), user.getFullName(), user.getUsername(), user.isMustChangePassword());
    }

    // Changes password for currently authenticated user and clears the mustChangePassword flag
    @Transactional
    public Map<String, String> changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Validate old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Current password does not match");
        }

        // Validate new password differs from old password
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        // Update password hash and reset the must-change-password flag
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);

        return Map.of("message", "Password changed successfully");
    }
}
