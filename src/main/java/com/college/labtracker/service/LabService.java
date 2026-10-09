package com.college.labtracker.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.college.labtracker.dto.lab.LabRequest;
import com.college.labtracker.dto.lab.LabResponse;
import com.college.labtracker.entity.Lab;
import com.college.labtracker.exception.ResourceNotFoundException;
import com.college.labtracker.repository.LabRepository;

// Service providing business logic for lab room management
@Service
public class LabService {

    private final LabRepository labRepository;

    public LabService(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    public LabResponse toResponse(Lab lab) {
        return new LabResponse(
                lab.getId(),
                lab.getName(),
                lab.getRoomNo(),
                lab.getSlotCapacity()
        );
    }

    // List all laboratories
    public List<LabResponse> getAllLabs() {
        return labRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get specific laboratory by ID
    public LabResponse getLabById(Long id) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + id));
        return toResponse(lab);
    }

    // Create new laboratory room (Admin only)
    @Transactional
    public LabResponse createLab(LabRequest request) {
        Lab lab = new Lab(
                request.getName(),
                request.getRoomNo(),
                request.getSlotCapacity()
        );
        Lab saved = labRepository.save(lab);
        return toResponse(saved);
    }

    // Update existing laboratory room (Admin only)
    @Transactional
    public LabResponse updateLab(Long id, LabRequest request) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + id));

        lab.setName(request.getName());
        lab.setRoomNo(request.getRoomNo());
        lab.setSlotCapacity(request.getSlotCapacity());

        Lab updated = labRepository.save(lab);
        return toResponse(updated);
    }

    // Delete laboratory room (Admin only)
    @Transactional
    public void deleteLab(Long id) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + id));
        labRepository.delete(lab);
    }
}
