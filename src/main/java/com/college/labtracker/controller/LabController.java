package com.college.labtracker.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.college.labtracker.dto.lab.LabRequest;
import com.college.labtracker.dto.lab.LabResponse;
import com.college.labtracker.service.LabService;
import jakarta.validation.Valid;

// REST controller exposing endpoints for laboratory room management
@RestController
@RequestMapping("/api/labs")
public class LabController {

    private final LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    // GET /api/labs: Retrieve list of all departmental labs (All authenticated users)
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LabResponse>> getAllLabs() {
        return ResponseEntity.ok(labService.getAllLabs());
    }

    // GET /api/labs/{id}: Get laboratory room details by ID (All authenticated users)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LabResponse> getLabById(@PathVariable Long id) {
        return ResponseEntity.ok(labService.getLabById(id));
    }

    // POST /api/labs: Register a new laboratory room (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LabResponse> createLab(@Valid @RequestBody LabRequest request) {
        LabResponse response = labService.createLab(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/labs/{id}: Update laboratory details (Admin only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LabResponse> updateLab(@PathVariable Long id, @Valid @RequestBody LabRequest request) {
        return ResponseEntity.ok(labService.updateLab(id, request));
    }

    // DELETE /api/labs/{id}: Remove a laboratory room (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteLab(@PathVariable Long id) {
        labService.deleteLab(id);
        return ResponseEntity.ok(Map.of("message", "Laboratory deleted successfully"));
    }
}
