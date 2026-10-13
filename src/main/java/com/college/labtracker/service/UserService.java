package com.college.labtracker.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.college.labtracker.dto.user.UserImportSummary;
import com.college.labtracker.dto.user.UserRequest;
import com.college.labtracker.dto.user.UserResponse;
import com.college.labtracker.entity.Lab;
import com.college.labtracker.entity.Role;
import com.college.labtracker.entity.User;
import com.college.labtracker.exception.DuplicateResourceException;
import com.college.labtracker.exception.ResourceNotFoundException;
import com.college.labtracker.repository.LabRepository;
import com.college.labtracker.repository.UserRepository;

// Service providing user management operations and configurable roll number validation
@Service
public class UserService {

    private final UserRepository userRepository;
    private final LabRepository labRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.rollno-pattern:^[0-9]{2}[a-zA-Z]{2}[0-9]{3}[a-zA-Z][0-9]{2}$}")
    private String rollNoPattern;

    @Value("${app.default-student-password:Student@123}")
    private String defaultStudentPassword;

    public UserService(UserRepository userRepository, LabRepository labRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.labRepository = labRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Convert User entity to UserResponse DTO
    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.isMustChangePassword()
        );
            response.setLoginCount(user.getLoginCount());
            response.setLastLoginAt(user.getLastLoginAt());
        if (user.getLab() != null) {
            response.setLabId(user.getLab().getId());
            response.setLabName(user.getLab().getName());
        }
        return response;
    }

    // Retrieve all users in the system (Admin only)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Retrieve a single user by ID
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return toResponse(user);
    }

    // Create a new user account with hashed password and role-based roll number validation
    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Roll number / Username '" + request.getUsername() + "' is already registered");
        }

        // Validate roll number format if role is STUDENT
        if (request.getRole() == Role.STUDENT) {
            if (!request.getUsername().matches(rollNoPattern)) {
                throw new IllegalArgumentException("Student roll number '" + request.getUsername() 
                        + "' does not match required format (e.g. 24eg104e29). Pattern: " + rollNoPattern);
            }
        }

        String rawPassword = (request.getPassword() != null && !request.getPassword().isBlank())
                ? request.getPassword()
                : (request.getRole() == Role.STUDENT ? defaultStudentPassword : "default123");

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(rawPassword),
                request.getFullName(),
                request.getEmail(),
                request.getRole(),
                request.isActive(),
                request.isMustChangePassword()
        );
        user.setLab(resolveLab(request));

        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    // Update existing user details
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // If username changed, ensure new username is not already taken
        if (!user.getUsername().equalsIgnoreCase(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already in use");
        }

        // Validate roll number format if role is STUDENT
        if (request.getRole() == Role.STUDENT) {
            if (!request.getUsername().matches(rollNoPattern)) {
                throw new IllegalArgumentException("Student roll number '" + request.getUsername() 
                        + "' does not match required format. Pattern: " + rollNoPattern);
            }
        }

        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setLab(resolveLab(request));
        user.setActive(request.isActive());
        user.setMustChangePassword(request.isMustChangePassword());

        // Update password if a new one is provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return toResponse(updatedUser);
    }

    private Lab resolveLab(UserRequest request) {
        if (request.getRole() != Role.LAB_INCHARGE && request.getRole() != Role.LAB_ASSISTANT) {
            return null;
        }
        if (request.getLabId() == null) {
            throw new IllegalArgumentException("A laboratory must be assigned to each lab staff account");
        }
        return labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + request.getLabId()));
    }

    // Deactivate a user account without deleting records
    @Transactional
    public UserResponse deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setActive(false);
        User updated = userRepository.save(user);
        return toResponse(updated);
    }

    // Bulk import student accounts from uploaded CSV file (Admin only)
    @Transactional
    public UserImportSummary importUsersFromCsv(MultipartFile file) {
        int created = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Skip header line if found
                if (lineNumber == 1 && (line.toLowerCase().startsWith("rollno") || line.toLowerCase().startsWith("username"))) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 3) {
                    errors.add("Line " + lineNumber + ": Invalid format. Expected 'rollNo,fullName,email'. Found: " + line);
                    continue;
                }

                String rollNo = parts[0].trim();
                String fullName = parts[1].trim();
                String email = parts[2].trim();

                if (rollNo.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                    errors.add("Line " + lineNumber + ": rollNo, fullName, and email cannot be empty.");
                    continue;
                }

                // Check roll number pattern
                if (!rollNo.matches(rollNoPattern)) {
                    errors.add("Line " + lineNumber + ": Roll number '" + rollNo + "' does not match required format (" + rollNoPattern + ")");
                    continue;
                }

                // Check if already registered
                if (userRepository.existsByUsername(rollNo)) {
                    skipped++;
                    continue;
                }

                // Create student account with default password and mustChangePassword = true
                User student = new User(
                        rollNo,
                        passwordEncoder.encode(defaultStudentPassword),
                        fullName,
                        email,
                        Role.STUDENT,
                        true,
                        true
                );
                userRepository.save(student);
                created++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded CSV file: " + e.getMessage(), e);
        }

        return new UserImportSummary(created, skipped, errors);
    }
}
