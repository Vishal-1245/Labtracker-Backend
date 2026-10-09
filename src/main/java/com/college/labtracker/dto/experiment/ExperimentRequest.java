package com.college.labtracker.dto.experiment;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO for creating and updating laboratory experiments
public class ExperimentRequest {

    @NotNull(message = "Lab ID is required")
    private Long labId;

    @NotBlank(message = "Experiment code is required (e.g. EDC-EXP-01)")
    @Size(max = 30, message = "Code must not exceed 30 characters")
    private String code;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;

    @NotBlank(message = "Aim is required")
    private String aim;

    @NotBlank(message = "Theory is required")
    private String theory;

    private String observationFormat;
    private String conclusionNote;
    private String safetyNotes;

    private List<ExperimentStepRequest> steps = new ArrayList<>();
    private List<ExperimentComponentRequest> components = new ArrayList<>();
    private List<ExperimentFormulaRequest> formulas = new ArrayList<>();

    public ExperimentRequest() {
    }

    public ExperimentRequest(Long labId, String code, String title, String aim, String theory, String observationFormat, String conclusionNote, String safetyNotes) {
        this.labId = labId;
        this.code = code;
        this.title = title;
        this.aim = aim;
        this.theory = theory;
        this.observationFormat = observationFormat;
        this.conclusionNote = conclusionNote;
        this.safetyNotes = safetyNotes;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
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

    public List<ExperimentStepRequest> getSteps() {
        return steps;
    }

    public void setSteps(List<ExperimentStepRequest> steps) {
        this.steps = steps;
    }

    public List<ExperimentComponentRequest> getComponents() {
        return components;
    }

    public void setComponents(List<ExperimentComponentRequest> components) {
        this.components = components;
    }

    public List<ExperimentFormulaRequest> getFormulas() {
        return formulas;
    }

    public void setFormulas(List<ExperimentFormulaRequest> formulas) {
        this.formulas = formulas;
    }
}
