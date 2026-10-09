package com.college.labtracker.dto.lab;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO for creating or updating a departmental laboratory
public class LabRequest {

    @NotBlank(message = "Lab name is required")
    @Size(max = 100, message = "Lab name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Room number is required")
    @Size(max = 20, message = "Room number must not exceed 20 characters")
    private String roomNo;

    @Min(value = 1, message = "Slot capacity must be at least 1")
    private int slotCapacity;

    public LabRequest() {
    }

    public LabRequest(String name, String roomNo, int slotCapacity) {
        this.name = name;
        this.roomNo = roomNo;
        this.slotCapacity = slotCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public int getSlotCapacity() {
        return slotCapacity;
    }

    public void setSlotCapacity(int slotCapacity) {
        this.slotCapacity = slotCapacity;
    }
}
