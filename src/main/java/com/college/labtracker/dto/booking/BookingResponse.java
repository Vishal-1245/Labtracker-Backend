package com.college.labtracker.dto.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import com.college.labtracker.entity.BookingStatus;

// DTO representing lab and equipment reservation details
public class BookingResponse {

    private Long id;
    private Long studentId;
    private String studentRollNo;
    private String studentName;
    private Long labId;
    private String labName;
    private Long experimentId;
    private String experimentCode;
    private String experimentTitle;
    private Long equipmentId;
    private String equipmentName;
    private String equipmentAssetTag;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String purpose;
    private BookingStatus status;

    public BookingResponse() {
    }

    public BookingResponse(Long id, Long studentId, String studentRollNo, String studentName, Long labId, String labName, Long experimentId, String experimentCode, String experimentTitle, Long equipmentId, String equipmentName, String equipmentAssetTag, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, String purpose, BookingStatus status) {
        this.id = id;
        this.studentId = studentId;
        this.studentRollNo = studentRollNo;
        this.studentName = studentName;
        this.labId = labId;
        this.labName = labName;
        this.experimentId = experimentId;
        this.experimentCode = experimentCode;
        this.experimentTitle = experimentTitle;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.equipmentAssetTag = equipmentAssetTag;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentRollNo() {
        return studentRollNo;
    }

    public void setStudentRollNo(String studentRollNo) {
        this.studentRollNo = studentRollNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentAssetTag() {
        return equipmentAssetTag;
    }

    public void setEquipmentAssetTag(String equipmentAssetTag) {
        this.equipmentAssetTag = equipmentAssetTag;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
