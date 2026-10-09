package com.college.labtracker.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.LabSession;
import com.college.labtracker.entity.SessionStatus;

@Repository
public interface LabSessionRepository extends JpaRepository<LabSession, Long> {
    List<LabSession> findBySessionDate(LocalDate sessionDate);
    List<LabSession> findByStatus(SessionStatus status);
    List<LabSession> findByLabId(Long labId);
}
