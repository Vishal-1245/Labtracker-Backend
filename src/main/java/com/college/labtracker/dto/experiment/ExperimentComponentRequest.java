package com.college.labtracker.dto.experiment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ExperimentComponentRequest {

    @NotNull(message = "Component ID is required")
    private Long componentId;

    @Min(value = 1, message = "Quantity per team must be at least 1")
    private int quantityPerTeam;

    public ExperimentComponentRequest() {
    }

    public ExperimentComponentRequest(Long componentId, int quantityPerTeam) {
        this.componentId = componentId;
        this.quantityPerTeam = quantityPerTeam;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public int getQuantityPerTeam() {
        return quantityPerTeam;
    }

    public void setQuantityPerTeam(int quantityPerTeam) {
        this.quantityPerTeam = quantityPerTeam;
    }
}
