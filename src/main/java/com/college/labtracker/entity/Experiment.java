package com.college.labtracker.entity;

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

// Experiment entity representing a curriculum experiment/practical
@Entity
@Table(name = "experiments")
public class Experiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @Column(nullable = false, length = 30)
    private String code;

    @Column(nullable = false, length = 150)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String aim;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String theory;

    @Lob
    @Column(name = "observation_format", columnDefinition = "TEXT")
    private String observationFormat;

    @Lob
    @Column(name = "conclusion_note", columnDefinition = "TEXT")
    private String conclusionNote;

    @Lob
    @Column(name = "safety_notes", columnDefinition = "TEXT")
    private String safetyNotes;

    public Experiment() {
    }

    public Experiment(Lab lab, String code, String title, String aim, String theory, String observationFormat, String conclusionNote, String safetyNotes) {
        this.lab = lab;
        this.code = code;
        this.title = title;
        this.aim = aim;
        this.theory = theory;
        this.observationFormat = observationFormat;
        this.conclusionNote = conclusionNote;
        this.safetyNotes = safetyNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lab getLab() {
        return lab;
    }

    public void setLab(Lab lab) {
        this.lab = lab;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

    public String getTheory() {
        return theory;
    }

    public void setTheory(String theory) {
        this.theory = theory;
    }

    public String getObservationFormat() {
        return observationFormat;
    }

    public void setObservationFormat(String observationFormat) {
        this.observationFormat = observationFormat;
    }

    public String getConclusionNote() {
        return conclusionNote;
    }

    public void setConclusionNote(String conclusionNote) {
        this.conclusionNote = conclusionNote;
    }

    public String getSafetyNotes() {
        return safetyNotes;
    }

    public void setSafetyNotes(String safetyNotes) {
        this.safetyNotes = safetyNotes;
    }
}
