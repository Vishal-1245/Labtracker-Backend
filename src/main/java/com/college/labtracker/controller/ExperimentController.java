package com.college.labtracker.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.college.labtracker.dto.experiment.ExperimentComponentRequest;
import com.college.labtracker.dto.experiment.ExperimentComponentResponse;
import com.college.labtracker.dto.experiment.ExperimentFormulaRequest;
import com.college.labtracker.dto.experiment.ExperimentFormulaResponse;
import com.college.labtracker.dto.experiment.ExperimentRequest;
import com.college.labtracker.dto.experiment.ExperimentResponse;
import com.college.labtracker.dto.experiment.ExperimentStepRequest;
import com.college.labtracker.dto.experiment.ExperimentStepResponse;
import com.college.labtracker.service.ExperimentService;
import jakarta.validation.Valid;

// REST controller exposing practical lab experiments, instructions, and required apparatus
@RestController
@RequestMapping("/api/experiments")
public class ExperimentController {

    private final ExperimentService experimentService;

    public ExperimentController(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    // GET /api/experiments: List experiments, optional filter by ?labId= (All authenticated users)
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExperimentResponse>> getAllExperiments(@RequestParam(required = false) Long labId) {
        return ResponseEntity.ok(experimentService.getAllExperiments(labId));
    }

    // GET /api/experiments/{id}: Get complete experiment manual with procedure steps, components, and formulas
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExperimentResponse> getExperimentById(@PathVariable Long id) {
        return ResponseEntity.ok(experimentService.getExperimentById(id));
    }

    // POST /api/experiments: Create new curricular experiment (Faculty / In-charge / Admin)
    @PostMapping
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ExperimentResponse> createExperiment(@Valid @RequestBody ExperimentRequest request) {
        ExperimentResponse response = experimentService.createExperiment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/experiments/{id}: Update experiment content
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ExperimentResponse> updateExperiment(
            @PathVariable Long id,
            @Valid @RequestBody ExperimentRequest request
    ) {
        return ResponseEntity.ok(experimentService.updateExperiment(id, request));
    }

    // DELETE /api/experiments/{id}: Delete experiment
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteExperiment(@PathVariable Long id) {
        experimentService.deleteExperiment(id);
        return ResponseEntity.ok(Map.of("message", "Experiment deleted successfully"));
    }

    // POST /api/experiments/{id}/steps: Append a procedural step
    @PostMapping("/{id}/steps")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ExperimentStepResponse> addStep(
            @PathVariable Long id,
            @Valid @RequestBody ExperimentStepRequest request
    ) {
        ExperimentStepResponse response = experimentService.addStep(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // DELETE /api/experiments/steps/{stepId}: Remove a step
    @DeleteMapping("/steps/{stepId}")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteStep(@PathVariable Long stepId) {
        experimentService.deleteStep(stepId);
        return ResponseEntity.ok(Map.of("message", "Experiment step deleted successfully"));
    }

    // POST /api/experiments/{id}/components: Add a component requirement
    @PostMapping("/{id}/components")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ExperimentComponentResponse> addComponent(
            @PathVariable Long id,
            @Valid @RequestBody ExperimentComponentRequest request
    ) {
        ExperimentComponentResponse response = experimentService.addComponent(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // DELETE /api/experiments/components/{id}: Remove a component requirement
    @DeleteMapping("/components/{id}")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteComponentRequirement(@PathVariable Long id) {
        experimentService.deleteComponentRequirement(id);
        return ResponseEntity.ok(Map.of("message", "Experiment component mapping deleted successfully"));
    }

    // POST /api/experiments/{id}/formulas: Add a calculation formula
    @PostMapping("/{id}/formulas")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ExperimentFormulaResponse> addFormula(
            @PathVariable Long id,
            @Valid @RequestBody ExperimentFormulaRequest request
    ) {
        ExperimentFormulaResponse response = experimentService.addFormula(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // DELETE /api/experiments/formulas/{formulaId}: Delete a formula
    @DeleteMapping("/formulas/{formulaId}")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteFormula(@PathVariable Long formulaId) {
        experimentService.deleteFormula(formulaId);
        return ResponseEntity.ok(Map.of("message", "Experiment formula deleted successfully"));
    }
}
