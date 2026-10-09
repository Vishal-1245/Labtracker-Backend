package com.college.labtracker.dto.equipment;

import jakarta.validation.constraints.NotBlank;

// DTO for reporting a fault or maintenance issue on an equipment asset
public class MaintenanceLogRequest {

    @NotBlank(message = "Issue description is required")
    private String issue;

    public MaintenanceLogRequest() {
    }

    public MaintenanceLogRequest(String issue) {
        this.issue = issue;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
