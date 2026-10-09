package com.college.labtracker.dto.experiment;

public class ExperimentComponentResponse {

    private Long id;
    private Long componentId;
    private String componentName;
    private String componentCategory;
    private int quantityPerTeam;

    public ExperimentComponentResponse() {
    }

    public ExperimentComponentResponse(Long id, Long componentId, String componentName, String componentCategory, int quantityPerTeam) {
        this.id = id;
        this.componentId = componentId;
        this.componentName = componentName;
        this.componentCategory = componentCategory;
        this.quantityPerTeam = quantityPerTeam;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentCategory() {
        return componentCategory;
    }

    public void setComponentCategory(String componentCategory) {
        this.componentCategory = componentCategory;
    }

    public int getQuantityPerTeam() {
        return quantityPerTeam;
    }

    public void setQuantityPerTeam(int quantityPerTeam) {
        this.quantityPerTeam = quantityPerTeam;
    }
}
