package com.college.labtracker.dto.component;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO for creating and updating component inventory records
public class ComponentRequest {

    @NotNull(message = "Lab ID is required")
    private Long labId;

    @NotBlank(message = "Component name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @Min(value = 0, message = "Total quantity cannot be negative")
    private int totalQuantity;

    @Min(value = 0, message = "Available quantity cannot be negative")
    private int availableQuantity;

    public ComponentRequest() {
    }

    public ComponentRequest(Long labId, String name, String category, int totalQuantity, int availableQuantity) {
        this.labId = labId;
        this.name = name;
        this.category = category;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
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
