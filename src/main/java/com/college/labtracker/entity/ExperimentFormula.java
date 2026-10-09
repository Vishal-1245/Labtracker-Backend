package com.college.labtracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// ExperimentFormula represents a formula to be calculated during lab experiments
@Entity
@Table(name = "experiment_formulas")
public class ExperimentFormula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    private Experiment experiment;

    @Column(nullable = false, length = 100)
    private String label;

    @Column(nullable = false, length = 255)
    private String expression;

    // Comma-separated variable names required for formula evaluation, e.g. "Vin,R1,R2"
    @Column(nullable = false, length = 255)
    private String variables;

    @Column(length = 30)
    private String unit;

    public ExperimentFormula() {
    }

    public ExperimentFormula(Experiment experiment, String label, String expression, String variables, String unit) {
        this.experiment = experiment;
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

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
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
