package com.college.labtracker.dto.session;

// DTO representing student attendance in a session
public class AttendanceResponse {

    private Long id;
    private Long sessionId;
    private Long studentId;
    private String studentRollNo;
    private String studentName;
    private boolean present;

    public AttendanceResponse() {
    }

    public AttendanceResponse(Long id, Long sessionId, Long studentId, String studentRollNo, String studentName, boolean present) {
        this.id = id;
        this.sessionId = sessionId;
        this.studentId = studentId;
        this.studentRollNo = studentRollNo;
        this.studentName = studentName;
        this.present = present;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
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

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
