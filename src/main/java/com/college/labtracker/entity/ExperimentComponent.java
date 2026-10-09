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

// ExperimentComponent maps which components and quantities are required per student team
@Entity
@Table(name = "experiment_components")
public class ExperimentComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    private Experiment experiment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "component_id", nullable = false)
    private Component component;

    @Column(name = "quantity_per_team", nullable = false)
    private int quantityPerTeam;

    public ExperimentComponent() {
    }

    public ExperimentComponent(Experiment experiment, Component component, int quantityPerTeam) {
        this.experiment = experiment;
        this.component = component;
        this.quantityPerTeam = quantityPerTeam;
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

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public int getQuantityPerTeam() {
        return quantityPerTeam;
    }

    public void setQuantityPerTeam(int quantityPerTeam) {
        this.quantityPerTeam = quantityPerTeam;
    }
}
