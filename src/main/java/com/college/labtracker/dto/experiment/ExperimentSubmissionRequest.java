package com.college.labtracker.dto.experiment;

import jakarta.validation.constraints.NotBlank;

public class ExperimentSubmissionRequest {

    @NotBlank
    private String submissionData;

    public String getSubmissionData() { return submissionData; }
    public void setSubmissionData(String submissionData) { this.submissionData = submissionData; }
}