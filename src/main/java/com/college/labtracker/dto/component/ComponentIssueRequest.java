package com.college.labtracker.dto.component;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// DTO for issuing components to a student project team
public class ComponentIssueRequest {

    @NotNull(message = "Team ID is required")
    private Long teamId;

    @NotNull(message = "Component ID is required")
    private Long componentId;

    @Min(value = 1, message = "Quantity issued must be at least 1")
    private int quantity;

    public ComponentIssueRequest() {
    }

    public ComponentIssueRequest(Long teamId, Long componentId, int quantity) {
        this.teamId = teamId;
        this.componentId = componentId;
        this.quantity = quantity;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
