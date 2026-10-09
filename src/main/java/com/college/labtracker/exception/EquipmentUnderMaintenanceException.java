package com.college.labtracker.exception;

// Thrown when an equipment item cannot be booked because it is under maintenance (HTTP 409)
public class EquipmentUnderMaintenanceException extends RuntimeException {
    public EquipmentUnderMaintenanceException(String message) {
        super(message);
    }
}
