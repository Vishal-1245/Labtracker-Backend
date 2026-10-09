package com.college.labtracker.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.college.labtracker.dto.experiment.ExperimentComponentRequest;
import com.college.labtracker.dto.experiment.ExperimentComponentResponse;
import com.college.labtracker.dto.experiment.ExperimentFormulaRequest;
import com.college.labtracker.dto.experiment.ExperimentFormulaResponse;
import com.college.labtracker.dto.experiment.ExperimentRequest;
import com.college.labtracker.dto.experiment.ExperimentResponse;
import com.college.labtracker.dto.experiment.ExperimentStepRequest;
import com.college.labtracker.dto.experiment.ExperimentStepResponse;
import com.college.labtracker.entity.Component;
import com.college.labtracker.entity.Experiment;
import com.college.labtracker.entity.ExperimentComponent;
import com.college.labtracker.entity.ExperimentFormula;
import com.college.labtracker.entity.ExperimentStep;
import com.college.labtracker.entity.Lab;
import com.college.labtracker.exception.ResourceNotFoundException;
import com.college.labtracker.repository.ComponentRepository;
import com.college.labtracker.repository.ExperimentComponentRepository;
import com.college.labtracker.repository.ExperimentFormulaRepository;
import com.college.labtracker.repository.ExperimentRepository;
import com.college.labtracker.repository.ExperimentStepRepository;
import com.college.labtracker.repository.LabRepository;

// Service handling curricular experiment manuals, steps, components, and formulas
@Service
public class ExperimentService {

    private final ExperimentRepository experimentRepository;
    private final ExperimentStepRepository stepRepository;
    private final ExperimentComponentRepository expComponentRepository;
    private final ExperimentFormulaRepository formulaRepository;
    private final LabRepository labRepository;
    private final ComponentRepository componentRepository;

    public ExperimentService(
            ExperimentRepository experimentRepository,
            ExperimentStepRepository stepRepository,
            ExperimentComponentRepository expComponentRepository,
            ExperimentFormulaRepository formulaRepository,
            LabRepository labRepository,
            ComponentRepository componentRepository
    ) {
        this.experimentRepository = experimentRepository;
        this.stepRepository = stepRepository;
        this.expComponentRepository = expComponentRepository;
        this.formulaRepository = formulaRepository;
        this.labRepository = labRepository;
        this.componentRepository = componentRepository;
    }

    public ExperimentResponse toResponse(Experiment experiment) {
        ExperimentResponse response = new ExperimentResponse(
                experiment.getId(),
                experiment.getLab().getId(),
                experiment.getLab().getName(),
                experiment.getCode(),
                experiment.getTitle(),
                experiment.getAim(),
                experiment.getTheory(),
                experiment.getObservationFormat(),
                experiment.getConclusionNote(),
                experiment.getSafetyNotes()
        );

        // Fetch ordered procedure steps
        List<ExperimentStepResponse> stepResponses = stepRepository.findByExperimentIdOrderByStepNoAsc(experiment.getId())
                .stream()
                .map(s -> new ExperimentStepResponse(s.getId(), s.getStepNo(), s.getInstruction()))
                .collect(Collectors.toList());
        response.setSteps(stepResponses);

        // Fetch required components
        List<ExperimentComponentResponse> compResponses = expComponentRepository.findByExperimentId(experiment.getId())
                .stream()
                .map(c -> new ExperimentComponentResponse(
                        c.getId(),
                        c.getComponent().getId(),
                        c.getComponent().getName(),
                        c.getComponent().getCategory(),
                        c.getQuantityPerTeam()))
                .collect(Collectors.toList());
        response.setComponents(compResponses);

        // Fetch formulas
        List<ExperimentFormulaResponse> formResponses = formulaRepository.findByExperimentId(experiment.getId())
                .stream()
                .map(f -> new ExperimentFormulaResponse(
                        f.getId(),
                        f.getLabel(),
                        f.getExpression(),
                        f.getVariables(),
                        f.getUnit()))
                .collect(Collectors.toList());
        response.setFormulas(formResponses);

        return response;
    }

    // List experiments, optionally filtered by lab ID
    public List<ExperimentResponse> getAllExperiments(Long labId) {
        List<Experiment> list = (labId != null)
                ? experimentRepository.findByLabId(labId)
                : experimentRepository.findAll();

        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Get detailed experiment by ID
    public ExperimentResponse getExperimentById(Long id) {
        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiment not found with ID: " + id));
        return toResponse(experiment);
    }

    // Create a new experiment
    @Transactional
    public ExperimentResponse createExperiment(ExperimentRequest request) {
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + request.getLabId()));

        Experiment experiment = new Experiment(
                lab,
                request.getCode(),
                request.getTitle(),
                request.getAim(),
                request.getTheory(),
                request.getObservationFormat(),
                request.getConclusionNote(),
                request.getSafetyNotes()
        );
        Experiment savedExp = experimentRepository.save(experiment);

        // Save procedure steps if provided
        if (request.getSteps() != null) {
            for (ExperimentStepRequest sReq : request.getSteps()) {
                ExperimentStep step = new ExperimentStep(savedExp, sReq.getStepNo(), sReq.getInstruction());
                stepRepository.save(step);
            }
        }

        // Save component requirements if provided
        if (request.getComponents() != null) {
            for (ExperimentComponentRequest cReq : request.getComponents()) {
                Component comp = componentRepository.findById(cReq.getComponentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Component not found with ID: " + cReq.getComponentId()));
                ExperimentComponent expComp = new ExperimentComponent(savedExp, comp, cReq.getQuantityPerTeam());
                expComponentRepository.save(expComp);
            }
        }

        // Save formulas if provided
        if (request.getFormulas() != null) {
            for (ExperimentFormulaRequest fReq : request.getFormulas()) {
                ExperimentFormula formula = new ExperimentFormula(
                        savedExp,
                        fReq.getLabel(),
                        fReq.getExpression(),
                        fReq.getVariables(),
                        fReq.getUnit()
                );
                formulaRepository.save(formula);
            }
        }

        return toResponse(savedExp);
    }

    // Update experiment details
    @Transactional
    public ExperimentResponse updateExperiment(Long id, ExperimentRequest request) {
        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiment not found with ID: " + id));

        if (!experiment.getLab().getId().equals(request.getLabId())) {
            Lab newLab = labRepository.findById(request.getLabId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + request.getLabId()));
            experiment.setLab(newLab);
        }

        experiment.setCode(request.getCode());
        experiment.setTitle(request.getTitle());
        experiment.setAim(request.getAim());
        experiment.setTheory(request.getTheory());
        experiment.setObservationFormat(request.getObservationFormat());
        experiment.setConclusionNote(request.getConclusionNote());
        experiment.setSafetyNotes(request.getSafetyNotes());

        Experiment updated = experimentRepository.save(experiment);
        return toResponse(updated);
    }

    // Delete experiment
    @Transactional
    public void deleteExperiment(Long id) {
        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiment not found with ID: " + id));
        experimentRepository.delete(experiment);
    }

    // Add a single step to an existing experiment
    @Transactional
    public ExperimentStepResponse addStep(Long experimentId, ExperimentStepRequest request) {
        Experiment experiment = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new ResourceNotFoundException("Experiment not found with ID: " + experimentId));
        ExperimentStep step = new ExperimentStep(experiment, request.getStepNo(), request.getInstruction());
        ExperimentStep saved = stepRepository.save(step);
        return new ExperimentStepResponse(saved.getId(), saved.getStepNo(), saved.getInstruction());
    }

    // Delete a step
    @Transactional
    public void deleteStep(Long stepId) {
        ExperimentStep step = stepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Step not found with ID: " + stepId));
        stepRepository.delete(step);
    }

    // Add component requirement to an experiment
    @Transactional
    public ExperimentComponentResponse addComponent(Long experimentId, ExperimentComponentRequest request) {
        Experiment experiment = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new ResourceNotFoundException("Experiment not found with ID: " + experimentId));
        Component component = componentRepository.findById(request.getComponentId())
                .orElseThrow(() -> new ResourceNotFoundException("Component not found with ID: " + request.getComponentId()));

        ExperimentComponent expComp = new ExperimentComponent(experiment, component, request.getQuantityPerTeam());
        ExperimentComponent saved = expComponentRepository.save(expComp);
        return new ExperimentComponentResponse(
                saved.getId(),
                component.getId(),
                component.getName(),
                component.getCategory(),
                saved.getQuantityPerTeam()
        );
    }

    // Delete component requirement
    @Transactional
    public void deleteComponentRequirement(Long id) {
        ExperimentComponent expComp = expComponentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiment component mapping not found with ID: " + id));
        expComponentRepository.delete(expComp);
    }

    // Add formula to experiment
    @Transactional
    public ExperimentFormulaResponse addFormula(Long experimentId, ExperimentFormulaRequest request) {
        Experiment experiment = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new ResourceNotFoundException("Experiment not found with ID: " + experimentId));

        ExperimentFormula formula = new ExperimentFormula(
                experiment,
                request.getLabel(),
                request.getExpression(),
                request.getVariables(),
                request.getUnit()
        );
        ExperimentFormula saved = formulaRepository.save(formula);
        return new ExperimentFormulaResponse(
                saved.getId(),
                saved.getLabel(),
                saved.getExpression(),
                saved.getVariables(),
                saved.getUnit()
        );
    }

    // Delete formula
    @Transactional
    public void deleteFormula(Long formulaId) {
        ExperimentFormula formula = formulaRepository.findById(formulaId)
                .orElseThrow(() -> new ResourceNotFoundException("Formula not found with ID: " + formulaId));
        formulaRepository.delete(formula);
    }
}
