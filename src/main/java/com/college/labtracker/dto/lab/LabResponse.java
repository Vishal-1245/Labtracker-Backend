package com.college.labtracker.dto.lab;

// DTO representing laboratory details returned by API endpoints
public class LabResponse {

    private Long id;
    private String name;
    private String roomNo;
    private int slotCapacity;

    public LabResponse() {
    }

    public LabResponse(Long id, String name, String roomNo, int slotCapacity) {
        this.id = id;
        this.name = name;
        this.roomNo = roomNo;
        this.slotCapacity = slotCapacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
