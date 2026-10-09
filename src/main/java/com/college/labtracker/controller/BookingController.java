package com.college.labtracker.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.college.labtracker.dto.booking.BookingRequest;
import com.college.labtracker.dto.booking.BookingResponse;
import com.college.labtracker.entity.BookingStatus;
import com.college.labtracker.service.BookingService;
import jakarta.validation.Valid;

// REST controller exposing lab slot and equipment reservations
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // POST /api/bookings: Reserve lab slot and apparatus
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponse> createBooking(
            Principal principal,
            @Valid @RequestBody BookingRequest request
    ) {
        BookingResponse response = bookingService.createBooking(request, principal.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/bookings/my: View reservation history for logged-in user
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Principal principal) {
        return ResponseEntity.ok(bookingService.getMyBookings(principal.getName()));
    }

    // GET /api/bookings: List bookings across laboratories for slot availability
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingResponse>> getAllBookings(
            @RequestParam(required = false) Long labId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) BookingStatus status
    ) {
        return ResponseEntity.ok(bookingService.getAllBookings(labId, date, status));
    }

    // PUT /api/bookings/{id}/cancel: Cancel an existing booking
    @PutMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id,
            Principal principal
    ) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, principal.getName()));
    }

    // PUT /api/bookings/{id}/complete: Mark a completed booking
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<BookingResponse> completeBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }
}
