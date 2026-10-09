package com.college.labtracker.dto.component;

// DTO representing component inventory stock levels
public class ComponentResponse {

    private Long id;
    private Long labId;
    private String labName;
    private String name;
    private String category;
    private int totalQuantity;
    private int availableQuantity;

    public ComponentResponse() {
    }

    public ComponentResponse(Long id, Long labId, String labName, String name, String category, int totalQuantity, int availableQuantity) {
        this.id = id;
        this.labId = labId;
        this.labName = labName;
        this.name = name;
        this.category = category;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
