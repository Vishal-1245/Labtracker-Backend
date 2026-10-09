package com.college.labtracker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findBySessionId(Long sessionId);
    List<Attendance> findByStudentId(Long studentId);
    Optional<Attendance> findBySessionIdAndStudentId(Long sessionId, Long studentId);
    boolean existsBySessionIdAndStudentId(Long sessionId, Long studentId);
    long countBySessionIdAndPresentTrue(Long sessionId);
}
