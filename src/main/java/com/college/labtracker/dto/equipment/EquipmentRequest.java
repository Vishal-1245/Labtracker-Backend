package com.college.labtracker.dto.equipment;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.college.labtracker.entity.EquipmentCategory;
import com.college.labtracker.entity.EquipmentStatus;

// DTO for creating or updating laboratory equipment
public class EquipmentRequest {

    @NotBlank(message = "Asset tag is required (e.g. ECE-DSO-01)")
    @Size(max = 50, message = "Asset tag must not exceed 50 characters")
    private String assetTag;

    @NotBlank(message = "Equipment name is required")
    @Size(max = 100, message = "Equipment name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Category is required")
    private EquipmentCategory category;

    @NotNull(message = "Lab ID is required")
    private Long labId;

    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    private LocalDate nextCalibrationDate;

    public EquipmentRequest() {
    }

    public EquipmentRequest(String assetTag, String name, EquipmentCategory category, Long labId, EquipmentStatus status, LocalDate nextCalibrationDate) {
        this.assetTag = assetTag;
        this.name = name;
        this.category = category;
        this.labId = labId;
        this.status = status;
        this.nextCalibrationDate = nextCalibrationDate;
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
