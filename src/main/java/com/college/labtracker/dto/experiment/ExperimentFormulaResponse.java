package com.college.labtracker.dto.experiment;

public class ExperimentFormulaResponse {

    private Long id;
    private String label;
    private String expression;
    private String variables;
    private String unit;

    public ExperimentFormulaResponse() {
    }

    public ExperimentFormulaResponse(Long id, String label, String expression, String variables, String unit) {
        this.id = id;
        this.label = label;
        this.expression = expression;
        this.variables = variables;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
