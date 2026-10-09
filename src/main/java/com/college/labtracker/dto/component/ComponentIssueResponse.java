package com.college.labtracker.dto.component;

import java.time.LocalDateTime;
import com.college.labtracker.entity.IssueStatus;

// DTO representing component issue transactions
public class ComponentIssueResponse {

    private Long id;
    private Long teamId;
    private String teamName;
    private Long componentId;
    private String componentName;
    private int quantityIssued;
    private int quantityReturned;
    private int quantityDamaged;
    private LocalDateTime issuedAt;
    private LocalDateTime returnedAt;
    private IssueStatus status;

    public ComponentIssueResponse() {
    }

    public ComponentIssueResponse(Long id, Long teamId, String teamName, Long componentId, String componentName, int quantityIssued, int quantityReturned, int quantityDamaged, LocalDateTime issuedAt, LocalDateTime returnedAt, IssueStatus status) {
        this.id = id;
        this.teamId = teamId;
        this.teamName = teamName;
        this.componentId = componentId;
        this.componentName = componentName;
        this.quantityIssued = quantityIssued;
        this.quantityReturned = quantityReturned;
        this.quantityDamaged = quantityDamaged;
        this.issuedAt = issuedAt;
        this.returnedAt = returnedAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public int getQuantityIssued() {
        return quantityIssued;
    }

    public void setQuantityIssued(int quantityIssued) {
        this.quantityIssued = quantityIssued;
    }

    public int getQuantityReturned() {
        return quantityReturned;
    }

    public void setQuantityReturned(int quantityReturned) {
        this.quantityReturned = quantityReturned;
    }

    public int getQuantityDamaged() {
        return quantityDamaged;
    }

    public void setQuantityDamaged(int quantityDamaged) {
        this.quantityDamaged = quantityDamaged;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }
}
