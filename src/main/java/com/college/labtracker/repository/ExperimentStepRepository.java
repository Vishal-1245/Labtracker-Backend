package com.college.labtracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.ExperimentStep;

@Repository
public interface ExperimentStepRepository extends JpaRepository<ExperimentStep, Long> {
    List<ExperimentStep> findByExperimentIdOrderByStepNoAsc(Long experimentId);
}
