package com.college.labtracker.dto.experiment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ExperimentStepRequest {

    @Min(value = 1, message = "Step number must be at least 1")
    private int stepNo;

    @NotBlank(message = "Instruction is required")
    private String instruction;

    public ExperimentStepRequest() {
    }

    public ExperimentStepRequest(int stepNo, String instruction) {
        this.stepNo = stepNo;
        this.instruction = instruction;
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
