package com.college.labtracker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.Team;
import com.college.labtracker.entity.User;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findBySessionId(Long sessionId);

    @Query("SELECT t FROM Team t JOIN t.members m WHERE t.session.id = :sessionId AND m.id = :userId")
    Optional<Team> findBySessionIdAndUserId(@Param("sessionId") Long sessionId, @Param("userId") Long userId);
}
