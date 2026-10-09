package com.college.labtracker.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.Lab;

@Repository
public interface LabRepository extends JpaRepository<Lab, Long> {
    Optional<Lab> findByRoomNo(String roomNo);
    boolean existsByRoomNo(String roomNo);
}
