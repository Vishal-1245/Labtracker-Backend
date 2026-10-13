package com.college.labtracker.dto.auth;

// Response payload returned upon successful login
public class LoginResponse {

    private String token;
    private String role;
    private String fullName;
    private String username;
    private boolean mustChangePassword;
    private Long assignedLabId;

    public LoginResponse() {
    }

    public LoginResponse(String token, String role, String fullName, String username, boolean mustChangePassword) {
        this(token, role, fullName, username, mustChangePassword, null);
    }

    public LoginResponse(String token, String role, String fullName, String username, boolean mustChangePassword, Long assignedLabId) {
        this.token = token;
        this.role = role;
        this.fullName = fullName;
        this.username = username;
        this.mustChangePassword = mustChangePassword;
        this.assignedLabId = assignedLabId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public Long getAssignedLabId() {
        return assignedLabId;
    }

    public void setAssignedLabId(Long assignedLabId) {
        this.assignedLabId = assignedLabId;
    }
}
