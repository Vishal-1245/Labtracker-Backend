package com.college.labtracker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.Experiment;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
    List<Experiment> findByLabId(Long labId);
    Optional<Experiment> findByCode(String code);
    boolean existsByCode(String code);
}
