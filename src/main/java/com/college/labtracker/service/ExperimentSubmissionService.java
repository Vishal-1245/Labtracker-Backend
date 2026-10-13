package com.college.labtracker.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.college.labtracker.dto.experiment.ExperimentSubmissionRequest;
import com.college.labtracker.dto.experiment.ExperimentSubmissionResponse;
import com.college.labtracker.entity.Experiment;
import com.college.labtracker.entity.ExperimentSubmission;
import com.college.labtracker.entity.Role;
import com.college.labtracker.entity.User;
import com.college.labtracker.exception.ResourceNotFoundException;
import com.college.labtracker.repository.ExperimentRepository;
import com.college.labtracker.repository.ExperimentSubmissionRepository;
import com.college.labtracker.repository.UserRepository;

@Service
public class ExperimentSubmissionService {

    private final ExperimentRepository experimentRepository;
    private final ExperimentSubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    public ExperimentSubmissionService(ExperimentRepository experimentRepository,
            ExperimentSubmissionRepository submissionRepository, UserRepository userRepository) {
        this.experimentRepository = experimentRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ExperimentSubmissionResponse submit(Long experimentId, String username, ExperimentSubmissionRequest request) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        if (student.getRole() != Role.STUDENT) {
            throw new AccessDeniedException("Only students can submit experiment results");
        }
        Experiment experiment = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new ResourceNotFoundException("Experiment not found with ID: " + experimentId));
        if (!request.getSubmissionData().stripLeading().startsWith("{")) {
            throw new IllegalArgumentException("Submission data must be a JSON object");
        }
        ExperimentSubmission saved = submissionRepository.save(
                new ExperimentSubmission(student, experiment, request.getSubmissionData()));
        return new ExperimentSubmissionResponse(saved.getId(), experiment.getId(), experiment.getTitle(), saved.getSubmittedAt());
    }
}