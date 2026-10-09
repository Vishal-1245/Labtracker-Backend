package com.college.labtracker.dto.session;

import jakarta.validation.constraints.NotNull;

// Individual student presence record
public class AttendanceRecordRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    private boolean present;

    public AttendanceRecordRequest() {
    }

    public AttendanceRecordRequest(Long studentId, boolean present) {
        this.studentId = studentId;
        this.present = present;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
