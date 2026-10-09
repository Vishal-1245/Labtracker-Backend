package com.college.labtracker.exception;

// Thrown when attempting to issue components beyond available stock (HTTP 400)
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
