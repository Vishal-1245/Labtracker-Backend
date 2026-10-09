package com.college.labtracker.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.college.labtracker.dto.equipment.EquipmentRequest;
import com.college.labtracker.dto.equipment.EquipmentResponse;
import com.college.labtracker.dto.equipment.MaintenanceLogRequest;
import com.college.labtracker.dto.equipment.MaintenanceLogResponse;
import com.college.labtracker.entity.Equipment;
import com.college.labtracker.entity.EquipmentCategory;
import com.college.labtracker.entity.EquipmentStatus;
import com.college.labtracker.entity.Lab;
import com.college.labtracker.entity.MaintenanceLog;
import com.college.labtracker.exception.DuplicateResourceException;
import com.college.labtracker.exception.ResourceNotFoundException;
import com.college.labtracker.repository.EquipmentRepository;
import com.college.labtracker.repository.LabRepository;
import com.college.labtracker.repository.MaintenanceLogRepository;

// Service handling laboratory equipment lifecycle, calibration tracking, and maintenance logs
@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final LabRepository labRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;

    public EquipmentService(
            EquipmentRepository equipmentRepository,
            LabRepository labRepository,
            MaintenanceLogRepository maintenanceLogRepository
    ) {
        this.equipmentRepository = equipmentRepository;
        this.labRepository = labRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
    }

    public EquipmentResponse toResponse(Equipment e) {
        return new EquipmentResponse(
                e.getId(),
                e.getAssetTag(),
                e.getName(),
                e.getCategory(),
                e.getLab().getId(),
                e.getLab().getName(),
                e.getStatus(),
                e.getNextCalibrationDate()
        );
    }

    public MaintenanceLogResponse toLogResponse(MaintenanceLog log) {
        return new MaintenanceLogResponse(
                log.getId(),
                log.getEquipment().getId(),
                log.getEquipment().getName(),
                log.getEquipment().getAssetTag(),
                log.getIssue(),
                log.getReportedDate(),
                log.getResolvedDate(),
                log.isResolved()
        );
    }

    // List all equipment with optional filters
    public List<EquipmentResponse> getAllEquipment(Long labId, EquipmentStatus status, EquipmentCategory category) {
        List<Equipment> list = equipmentRepository.findAll();

        if (labId != null) {
            list = list.stream().filter(e -> e.getLab().getId().equals(labId)).collect(Collectors.toList());
        }
        if (status != null) {
            list = list.stream().filter(e -> e.getStatus() == status).collect(Collectors.toList());
        }
        if (category != null) {
            list = list.stream().filter(e -> e.getCategory() == category).collect(Collectors.toList());
        }

        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Retrieve single equipment by ID
    public EquipmentResponse getEquipmentById(Long id) {
        Equipment e = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + id));
        return toResponse(e);
    }

    // Create new equipment asset
    @Transactional
    public EquipmentResponse createEquipment(EquipmentRequest request) {
        if (equipmentRepository.existsByAssetTag(request.getAssetTag())) {
            throw new DuplicateResourceException("Asset tag '" + request.getAssetTag() + "' already exists");
        }

        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + request.getLabId()));

        Equipment equipment = new Equipment(
                request.getAssetTag(),
                request.getName(),
                request.getCategory(),
                lab,
                request.getStatus() != null ? request.getStatus() : EquipmentStatus.AVAILABLE,
                request.getNextCalibrationDate()
        );

        Equipment saved = equipmentRepository.save(equipment);
        return toResponse(saved);
    }

    // Update equipment details
    @Transactional
    public EquipmentResponse updateEquipment(Long id, EquipmentRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + id));

        if (!equipment.getAssetTag().equalsIgnoreCase(request.getAssetTag()) && equipmentRepository.existsByAssetTag(request.getAssetTag())) {
            throw new DuplicateResourceException("Asset tag '" + request.getAssetTag() + "' is already in use");
        }

        if (!equipment.getLab().getId().equals(request.getLabId())) {
            Lab newLab = labRepository.findById(request.getLabId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + request.getLabId()));
            equipment.setLab(newLab);
        }

        equipment.setAssetTag(request.getAssetTag());
        equipment.setName(request.getName());
        equipment.setCategory(request.getCategory());
        if (request.getStatus() != null) {
            equipment.setStatus(request.getStatus());
        }
        equipment.setNextCalibrationDate(request.getNextCalibrationDate());

        Equipment updated = equipmentRepository.save(equipment);
        return toResponse(updated);
    }

    // Update equipment operational status
    @Transactional
    public EquipmentResponse updateStatus(Long id, EquipmentStatus status) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + id));
        equipment.setStatus(status);
        Equipment updated = equipmentRepository.save(equipment);
        return toResponse(updated);
    }

    // Report a fault or issue: puts equipment in MAINTENANCE and opens a MaintenanceLog
    @Transactional
    public MaintenanceLogResponse reportMaintenance(Long equipmentId, MaintenanceLogRequest request) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + equipmentId));

        // Mark equipment status as undergoing maintenance
        equipment.setStatus(EquipmentStatus.UNDER_MAINTENANCE);
        equipmentRepository.save(equipment);

        MaintenanceLog log = new MaintenanceLog(
                equipment,
                request.getIssue(),
                LocalDateTime.now(),
                null,
                false
        );
        MaintenanceLog savedLog = maintenanceLogRepository.save(log);
        return toLogResponse(savedLog);
    }

    // Resolve an equipment maintenance ticket: marks log resolved and restores status to AVAILABLE
    @Transactional
    public MaintenanceLogResponse resolveMaintenance(Long logId) {
        MaintenanceLog log = maintenanceLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance log not found with ID: " + logId));

        log.setResolved(true);
        log.setResolvedDate(LocalDateTime.now());
        MaintenanceLog savedLog = maintenanceLogRepository.save(log);

        // Restore equipment availability
        Equipment equipment = log.getEquipment();
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipmentRepository.save(equipment);

        return toLogResponse(savedLog);
    }

    // List maintenance logs with optional equipment and resolution filters
    public List<MaintenanceLogResponse> getMaintenanceLogs(Long equipmentId, Boolean resolved) {
        List<MaintenanceLog> logs = (equipmentId != null)
                ? maintenanceLogRepository.findByEquipmentId(equipmentId)
                : maintenanceLogRepository.findAll();

        if (resolved != null) {
            logs = logs.stream().filter(l -> l.isResolved() == resolved).collect(Collectors.toList());
        }

        return logs.stream().map(this::toLogResponse).collect(Collectors.toList());
    }
}
