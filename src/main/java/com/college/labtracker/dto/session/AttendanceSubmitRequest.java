package com.college.labtracker.dto.session;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;

// DTO for batch attendance submission for a lab session
public class AttendanceSubmitRequest {

    @NotEmpty(message = "Attendance list cannot be empty")
    private List<AttendanceRecordRequest> records = new ArrayList<>();

    public AttendanceSubmitRequest() {
    }

    public AttendanceSubmitRequest(List<AttendanceRecordRequest> records) {
        this.records = records;
    }

    public List<AttendanceRecordRequest> getRecords() {
        return records;
    }

    public void setRecords(List<AttendanceRecordRequest> records) {
        this.records = records;
    }
}
