package com.college.labtracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.ExperimentFormula;

@Repository
public interface ExperimentFormulaRepository extends JpaRepository<ExperimentFormula, Long> {
    List<ExperimentFormula> findByExperimentId(Long experimentId);
}
