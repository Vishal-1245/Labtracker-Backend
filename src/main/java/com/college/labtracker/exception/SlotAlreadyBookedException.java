package com.college.labtracker.exception;

// Thrown when lab capacity is full or equipment is already reserved for the requested slot (HTTP 409)
public class SlotAlreadyBookedException extends RuntimeException {
    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}
