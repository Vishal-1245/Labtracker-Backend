package com.college.labtracker.dto.user;

import com.college.labtracker.entity.Role;

// Response DTO containing public user information (excluding password hash)
public class UserResponse {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Role role;
    private boolean active;
    private boolean mustChangePassword;

    public UserResponse() {
    }

    public UserResponse(Long id, String username, String fullName, String email, Role role, boolean active, boolean mustChangePassword) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.active = active;
        this.mustChangePassword = mustChangePassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }
}
