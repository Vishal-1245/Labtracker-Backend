package com.college.labtracker.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.college.labtracker.dto.error.ErrorResponse;

// Centralized exception handler providing clean, uniform JSON responses across all controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400: DTO validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ErrorResponse response = new ErrorResponse("Validation failed. Please correct the highlighted fields.", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 404: Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 409: Duplicate resource (e.g. roll number already registered, asset tag collision)
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 409: Slot capacity reached or equipment double-booked
    @ExceptionHandler(SlotAlreadyBookedException.class)
    public ResponseEntity<ErrorResponse> handleSlotAlreadyBookedException(SlotAlreadyBookedException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 409: Equipment is undergoing maintenance
    @ExceptionHandler(EquipmentUnderMaintenanceException.class)
    public ResponseEntity<ErrorResponse> handleEquipmentUnderMaintenanceException(EquipmentUnderMaintenanceException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 400: Not enough component stock in the lab
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(InsufficientStockException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400: Invalid booking timings or constraints
    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBookingException(InvalidBookingException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 401: Invalid username or password
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorResponse response = new ErrorResponse("Invalid username or password", null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 401: Inactive user account
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException ex) {
        ErrorResponse response = new ErrorResponse("Your account is deactivated. Please contact the administrator.", null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 403: Role permission denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse response = new ErrorResponse("Access Denied: You do not have permission to access this resource", null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // 400: Illegal argument or business constraint
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 500: Unexpected internal server errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse response = new ErrorResponse("An internal server error occurred: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
