package com.college.labtracker.dto.experiment;

public class ExperimentStepResponse {

    private Long id;
    private int stepNo;
    private String instruction;

    public ExperimentStepResponse() {
    }

    public ExperimentStepResponse(Long id, int stepNo, String instruction) {
        this.id = id;
        this.stepNo = stepNo;
        this.instruction = instruction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
