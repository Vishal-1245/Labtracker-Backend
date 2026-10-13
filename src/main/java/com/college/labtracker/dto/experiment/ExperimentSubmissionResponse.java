package com.college.labtracker.dto.experiment;

import java.time.LocalDateTime;

public class ExperimentSubmissionResponse {
    private final Long id;
    private final Long experimentId;
    private final String experimentTitle;
    private final LocalDateTime submittedAt;

    public ExperimentSubmissionResponse(Long id, Long experimentId, String experimentTitle, LocalDateTime submittedAt) {
        this.id = id;
        this.experimentId = experimentId;
        this.experimentTitle = experimentTitle;
        this.submittedAt = submittedAt;
    }

    public Long getId() { return id; }
    public Long getExperimentId() { return experimentId; }
    public String getExperimentTitle() { return experimentTitle; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
}