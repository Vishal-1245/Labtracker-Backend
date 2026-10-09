package com.college.labtracker.dto.component;

import jakarta.validation.constraints.Min;

public class ComponentRestockRequest {

    @Min(value = 1, message = "Quantity to add must be at least 1")
    private int quantityToAdd;

    public ComponentRestockRequest() {
    }

    public ComponentRestockRequest(int quantityToAdd) {
        this.quantityToAdd = quantityToAdd;
    }

    public int getQuantityToAdd() {
        return quantityToAdd;
    }

    public void setQuantityToAdd(int quantityToAdd) {
        this.quantityToAdd = quantityToAdd;
    }
}
