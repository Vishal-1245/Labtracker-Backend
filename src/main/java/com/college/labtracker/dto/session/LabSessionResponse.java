package com.college.labtracker.dto.session;

import java.time.LocalDate;
import java.time.LocalTime;
import com.college.labtracker.entity.SessionStatus;

// DTO representing lab class session information
public class LabSessionResponse {

    private Long id;
    private Long labId;
    private String labName;
    private Long experimentId;
    private String experimentCode;
    private String experimentTitle;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String batch;
    private Long conductedById;
    private String conductedByName;
    private SessionStatus status;

    public LabSessionResponse() {
    }

    public LabSessionResponse(Long id, Long labId, String labName, Long experimentId, String experimentCode, String experimentTitle, LocalDate sessionDate, LocalTime startTime, LocalTime endTime, String batch, Long conductedById, String conductedByName, SessionStatus status) {
        this.id = id;
        this.labId = labId;
        this.labName = labName;
        this.experimentId = experimentId;
        this.experimentCode = experimentCode;
        this.experimentTitle = experimentTitle;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.batch = batch;
        this.conductedById = conductedById;
        this.conductedByName = conductedByName;
        this.status = status;
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

    public Long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

    public String getExperimentCode() {
        return experimentCode;
    }

    public void setExperimentCode(String experimentCode) {
        this.experimentCode = experimentCode;
    }

    public String getExperimentTitle() {
        return experimentTitle;
    }

    public void setExperimentTitle(String experimentTitle) {
        this.experimentTitle = experimentTitle;
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

    public String getConductedByName() {
        return conductedByName;
    }

    public void setConductedByName(String conductedByName) {
        this.conductedByName = conductedByName;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}
