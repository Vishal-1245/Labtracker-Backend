package com.college.labtracker.exception;

// Thrown when an entity (User, Lab, Equipment, etc.) is not found in database (HTTP 404)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
