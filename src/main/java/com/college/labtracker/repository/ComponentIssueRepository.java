package com.college.labtracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.ComponentIssue;
import com.college.labtracker.entity.IssueStatus;

@Repository
public interface ComponentIssueRepository extends JpaRepository<ComponentIssue, Long> {
    List<ComponentIssue> findByTeamId(Long teamId);
    List<ComponentIssue> findByStatus(IssueStatus status);
    List<ComponentIssue> findByTeamSessionId(Long sessionId);
}
