package com.college.labtracker.controller;

import java.util.List;
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
import com.college.labtracker.dto.equipment.EquipmentRequest;
import com.college.labtracker.dto.equipment.EquipmentResponse;
import com.college.labtracker.dto.equipment.MaintenanceLogRequest;
import com.college.labtracker.dto.equipment.MaintenanceLogResponse;
import com.college.labtracker.entity.EquipmentCategory;
import com.college.labtracker.entity.EquipmentStatus;
import com.college.labtracker.service.EquipmentService;
import jakarta.validation.Valid;

// REST controller exposing equipment inventory, calibration tracking, and maintenance logs
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // GET /api/equipment: List equipment with optional filters for lab, status, and category
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EquipmentResponse>> getAllEquipment(
            @RequestParam(required = false) Long labId,
            @RequestParam(required = false) EquipmentStatus status,
            @RequestParam(required = false) EquipmentCategory category
    ) {
        return ResponseEntity.ok(equipmentService.getAllEquipment(labId, status, category));
    }

    // GET /api/equipment/{id}: Retrieve detailed record for a specific asset
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    // POST /api/equipment: Register new equipment into laboratory inventory
    @PostMapping
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<EquipmentResponse> createEquipment(@Valid @RequestBody EquipmentRequest request) {
        EquipmentResponse response = equipmentService.createEquipment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/equipment/{id}: Update equipment specification and asset tag
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentRequest request
    ) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, request));
    }

    // PUT /api/equipment/{id}/status: Change equipment operational status directly
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<EquipmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam EquipmentStatus status
    ) {
        return ResponseEntity.ok(equipmentService.updateStatus(id, status));
    }

    // POST /api/equipment/{id}/maintenance: Report a defect/malfunction and flag asset for maintenance
    @PostMapping("/{id}/maintenance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MaintenanceLogResponse> reportMaintenance(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceLogRequest request
    ) {
        MaintenanceLogResponse response = equipmentService.reportMaintenance(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/equipment/maintenance/{logId}/resolve: Mark maintenance log resolved and restore equipment to AVAILABLE
    @PutMapping("/maintenance/{logId}/resolve")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<MaintenanceLogResponse> resolveMaintenance(@PathVariable Long logId) {
        return ResponseEntity.ok(equipmentService.resolveMaintenance(logId));
    }

    // GET /api/equipment/maintenance: View maintenance audit trail and active defects broadcast
    @GetMapping("/maintenance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MaintenanceLogResponse>> getMaintenanceLogs(
            @RequestParam(required = false) Long equipmentId,
            @RequestParam(required = false) Boolean resolved
    ) {
        return ResponseEntity.ok(equipmentService.getMaintenanceLogs(equipmentId, resolved));
    }
}
