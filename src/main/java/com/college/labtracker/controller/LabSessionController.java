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
import com.college.labtracker.dto.session.AttendanceResponse;
import com.college.labtracker.dto.session.AttendanceSubmitRequest;
import com.college.labtracker.dto.session.LabSessionRequest;
import com.college.labtracker.dto.session.LabSessionResponse;
import com.college.labtracker.dto.session.TeamCreateRequest;
import com.college.labtracker.dto.session.TeamResponse;
import com.college.labtracker.entity.SessionStatus;
import com.college.labtracker.service.LabSessionService;
import jakarta.validation.Valid;

// REST controller exposing practical class scheduling, team formation, and student attendance
@RestController
@RequestMapping("/api/sessions")
public class LabSessionController {

    private final LabSessionService sessionService;

    public LabSessionController(LabSessionService sessionService) {
        this.sessionService = sessionService;
    }

    // GET /api/sessions: List sessions with optional filters for lab, date, and status
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LabSessionResponse>> getAllSessions(
            @RequestParam(required = false) Long labId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) SessionStatus status
    ) {
        return ResponseEntity.ok(sessionService.getAllSessions(labId, date, status));
    }

    // GET /api/sessions/{id}: Get detailed session information
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LabSessionResponse> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    // POST /api/sessions: Schedule a lab session (Faculty / Lab in-charge / Admin)
    @PostMapping
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<LabSessionResponse> createSession(
            Principal principal,
            @Valid @RequestBody LabSessionRequest request
    ) {
        LabSessionResponse response = sessionService.createSession(request, principal.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/sessions/{id}/status: Transition session lifecycle (OPEN -> IN_PROGRESS -> COMPLETED)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<LabSessionResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam SessionStatus status
    ) {
        return ResponseEntity.ok(sessionService.updateStatus(id, status));
    }

    // POST /api/sessions/{id}/attendance: Batch submit student attendance records
    @PostMapping("/{id}/attendance")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<List<AttendanceResponse>> submitAttendance(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceSubmitRequest request
    ) {
        List<AttendanceResponse> response = sessionService.submitAttendance(id, request);
        return ResponseEntity.ok(response);
    }

    // GET /api/sessions/{id}/attendance: Retrieve recorded attendance list for a session
    @GetMapping("/{id}/attendance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AttendanceResponse>> getAttendance(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getAttendance(id));
    }

    // POST /api/sessions/{id}/teams: Form a student working team for this practical session
    @PostMapping("/{id}/teams")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeamResponse> createTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamCreateRequest request
    ) {
        TeamResponse response = sessionService.createTeam(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/sessions/{id}/teams: Retrieve all teams registered in this session
    @GetMapping("/{id}/teams")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TeamResponse>> getTeams(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getTeams(id));
    }
}
