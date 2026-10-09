package com.college.labtracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.ExperimentComponent;

@Repository
public interface ExperimentComponentRepository extends JpaRepository<ExperimentComponent, Long> {
    List<ExperimentComponent> findByExperimentId(Long experimentId);
}
