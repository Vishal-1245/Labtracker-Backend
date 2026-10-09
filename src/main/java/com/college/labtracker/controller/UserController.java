package com.college.labtracker.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.college.labtracker.dto.user.UserImportSummary;
import com.college.labtracker.dto.user.UserRequest;
import com.college.labtracker.dto.user.UserResponse;
import com.college.labtracker.repository.UserRepository;
import com.college.labtracker.service.UserService;
import jakarta.validation.Valid;

// REST controller managing system users and bulk user operations (Admin role protected)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // GET /api/users: List all users (ADMIN, LAB_INCHARGE for attendance & teams)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_INCHARGE')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/{id}: Fetch user by ID (ADMIN, LAB_INCHARGE)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_INCHARGE')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // POST /api/users: Create user account (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/users/{id}: Update user account (ADMIN only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // PUT /api/users/{id}/deactivate: Deactivate user account (ADMIN only)
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }

    // POST /api/users/import: Bulk import students from CSV (ADMIN only)
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserImportSummary> importUsers(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file must be provided and cannot be empty");
        }
        UserImportSummary summary = userService.importUsersFromCsv(file);
        return ResponseEntity.ok(summary);
    }

    // GET /api/users/me: Get currently logged-in user details (Any authenticated user)
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        var user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Logged in user not found: " + principal.getName()));
        return ResponseEntity.ok(userService.toResponse(user));
    }
}
