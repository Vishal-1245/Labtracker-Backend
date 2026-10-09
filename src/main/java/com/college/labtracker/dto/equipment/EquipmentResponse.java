package com.college.labtracker.dto.equipment;

import java.time.LocalDate;
import com.college.labtracker.entity.EquipmentCategory;
import com.college.labtracker.entity.EquipmentStatus;

// DTO representing equipment asset details returned to the frontend
public class EquipmentResponse {

    private Long id;
    private String assetTag;
    private String name;
    private EquipmentCategory category;
    private Long labId;
    private String labName;
    private EquipmentStatus status;
    private LocalDate nextCalibrationDate;

    public EquipmentResponse() {
    }

    public EquipmentResponse(Long id, String assetTag, String name, EquipmentCategory category, Long labId, String labName, EquipmentStatus status, LocalDate nextCalibrationDate) {
        this.id = id;
        this.assetTag = assetTag;
        this.name = name;
        this.category = category;
        this.labId = labId;
        this.labName = labName;
        this.status = status;
        this.nextCalibrationDate = nextCalibrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EquipmentCategory getCategory() {
        return category;
    }

    public void setCategory(EquipmentCategory category) {
        this.category = category;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public LocalDate getNextCalibrationDate() {
        return nextCalibrationDate;
    }

    public void setNextCalibrationDate(LocalDate nextCalibrationDate) {
        this.nextCalibrationDate = nextCalibrationDate;
    }
}
