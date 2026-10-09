package com.college.labtracker.dto.experiment;

import java.util.ArrayList;
import java.util.List;

// Response DTO containing full experiment specifications, procedures, and component bills
public class ExperimentResponse {

    private Long id;
    private Long labId;
    private String labName;
    private String code;
    private String title;
    private String aim;
    private String theory;
    private String observationFormat;
    private String conclusionNote;
    private String safetyNotes;

    private List<ExperimentStepResponse> steps = new ArrayList<>();
    private List<ExperimentComponentResponse> components = new ArrayList<>();
    private List<ExperimentFormulaResponse> formulas = new ArrayList<>();

    public ExperimentResponse() {
    }

    public ExperimentResponse(Long id, Long labId, String labName, String code, String title, String aim, String theory, String observationFormat, String conclusionNote, String safetyNotes) {
        this.id = id;
        this.labId = labId;
        this.labName = labName;
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

    public List<ExperimentStepResponse> getSteps() {
        return steps;
    }

    public void setSteps(List<ExperimentStepResponse> steps) {
        this.steps = steps;
    }

    public List<ExperimentComponentResponse> getComponents() {
        return components;
    }

    public void setComponents(List<ExperimentComponentResponse> components) {
        this.components = components;
    }

    public List<ExperimentFormulaResponse> getFormulas() {
        return formulas;
    }

    public void setFormulas(List<ExperimentFormulaResponse> formulas) {
        this.formulas = formulas;
    }
}
