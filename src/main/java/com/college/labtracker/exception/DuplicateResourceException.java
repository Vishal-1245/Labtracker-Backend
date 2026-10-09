package com.college.labtracker.exception;

// Thrown when an entity with duplicate unique keys (roll number, asset tag, duplicate attendance) is submitted (HTTP 409)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
