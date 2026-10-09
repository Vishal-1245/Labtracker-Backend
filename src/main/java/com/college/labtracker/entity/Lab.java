package com.college.labtracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Lab entity representing a departmental laboratory room
@Entity
@Table(name = "labs")
public class Lab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "room_no", nullable = false, length = 20)
    private String roomNo;

    @Column(name = "slot_capacity", nullable = false)
    private int slotCapacity;

    public Lab() {
    }

    public Lab(String name, String roomNo, int slotCapacity) {
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
