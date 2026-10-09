package com.college.labtracker.exception;

// Thrown when booking timings, dates, or parameters violate business rules (HTTP 400)
public class InvalidBookingException extends RuntimeException {
    public InvalidBookingException(String message) {
        super(message);
    }
}
