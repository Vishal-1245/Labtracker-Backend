package com.college.labtracker.dto.experiment;

import jakarta.validation.constraints.NotBlank;

public class ExperimentFormulaRequest {

    @NotBlank(message = "Formula label is required")
    private String label;

    @NotBlank(message = "Expression is required")
    private String expression;

    @NotBlank(message = "Variables list is required (comma-separated, e.g. Vm,Im)")
    private String variables;

    private String unit;

    public ExperimentFormulaRequest() {
    }

    public ExperimentFormulaRequest(String label, String expression, String variables, String unit) {
        this.label = label;
        this.expression = expression;
        this.variables = variables;
        this.unit = unit;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
