package com.college.labtracker.dto.session;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.college.labtracker.entity.SessionStatus;

// DTO for scheduling a new laboratory class session
public class LabSessionRequest {

    @NotNull(message = "Lab ID is required")
    private Long labId;

    @NotNull(message = "Experiment ID is required")
    private Long experimentId;

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Batch name is required (e.g. ECE-A-Batch1)")
    private String batch;

    private Long conductedById;

    private SessionStatus status = SessionStatus.OPEN;

    public LabSessionRequest() {
    }

    public LabSessionRequest(Long labId, Long experimentId, LocalDate sessionDate, LocalTime startTime, LocalTime endTime, String batch, Long conductedById, SessionStatus status) {
        this.labId = labId;
        this.experimentId = experimentId;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.batch = batch;
        this.conductedById = conductedById;
        this.status = status;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
    }

    public Long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Long getConductedById() {
        return conductedById;
    }

    public void setConductedById(Long conductedById) {
        this.conductedById = conductedById;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}
