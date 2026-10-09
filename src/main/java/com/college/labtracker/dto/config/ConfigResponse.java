package com.college.labtracker.dto.config;

// Response DTO exposing public institutional configuration
public class ConfigResponse {

    private String collegeName;
    private String departmentName;
    private String rollNoPattern;

    public ConfigResponse() {
    }

    public ConfigResponse(String collegeName, String departmentName, String rollNoPattern) {
        this.collegeName = collegeName;
        this.departmentName = departmentName;
        this.rollNoPattern = rollNoPattern;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRollNoPattern() {
        return rollNoPattern;
    }

    public void setRollNoPattern(String rollNoPattern) {
        this.rollNoPattern = rollNoPattern;
    }
}
