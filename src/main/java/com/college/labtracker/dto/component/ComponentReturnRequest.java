package com.college.labtracker.dto.component;

import jakarta.validation.constraints.Min;

// DTO for returning issued components with damage reconciliation
public class ComponentReturnRequest {

    @Min(value = 0, message = "Returned quantity cannot be negative")
    private int quantityReturned;

    @Min(value = 0, message = "Damaged quantity cannot be negative")
    private int quantityDamaged;

    public ComponentReturnRequest() {
    }

    public ComponentReturnRequest(int quantityReturned, int quantityDamaged) {
        this.quantityReturned = quantityReturned;
        this.quantityDamaged = quantityDamaged;
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
}
