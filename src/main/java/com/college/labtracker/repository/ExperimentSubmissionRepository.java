package com.college.labtracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.college.labtracker.entity.ExperimentSubmission;

public interface ExperimentSubmissionRepository extends JpaRepository<ExperimentSubmission, Long> {
    List<ExperimentSubmission> findByStudentIdOrderBySubmittedAtDesc(Long studentId);
}