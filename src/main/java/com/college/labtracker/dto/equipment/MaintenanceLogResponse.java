package com.college.labtracker.dto.equipment;

import java.time.LocalDateTime;

// DTO representing equipment maintenance event details
public class MaintenanceLogResponse {

    private Long id;
    private Long equipmentId;
    private String equipmentName;
    private String assetTag;
    private String issue;
    private LocalDateTime reportedDate;
    private LocalDateTime resolvedDate;
    private boolean resolved;

    public MaintenanceLogResponse() {
    }

    public MaintenanceLogResponse(Long id, Long equipmentId, String equipmentName, String assetTag, String issue, LocalDateTime reportedDate, LocalDateTime resolvedDate, boolean resolved) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.assetTag = assetTag;
        this.issue = issue;
        this.reportedDate = reportedDate;
        this.resolvedDate = resolvedDate;
        this.resolved = resolved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public LocalDateTime getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDateTime reportedDate) {
        this.reportedDate = reportedDate;
    }

    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
