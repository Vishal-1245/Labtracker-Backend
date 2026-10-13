package com.college.labtracker.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "experiment_submissions")
public class ExperimentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "experiment_id", nullable = false)
    private Experiment experiment;

    @Lob
    @Column(name = "submission_data", columnDefinition = "TEXT", nullable = false)
    private String submissionData;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    public ExperimentSubmission() {
    }

    public ExperimentSubmission(User student, Experiment experiment, String submissionData) {
        this.student = student;
        this.experiment = experiment;
        this.submissionData = submissionData;
    }

    public Long getId() { return id; }
    public User getStudent() { return student; }
    public Experiment getExperiment() { return experiment; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
}