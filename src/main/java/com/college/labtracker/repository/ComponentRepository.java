package com.college.labtracker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.Component;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    List<Component> findByLabId(Long labId);
    Optional<Component> findFirstByName(String name);
}
