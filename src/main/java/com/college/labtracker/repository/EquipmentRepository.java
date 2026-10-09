package com.college.labtracker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.Equipment;
import com.college.labtracker.entity.EquipmentCategory;
import com.college.labtracker.entity.EquipmentStatus;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByLabId(Long labId);
    List<Equipment> findByStatus(EquipmentStatus status);
    List<Equipment> findByCategory(EquipmentCategory category);
    Optional<Equipment> findByAssetTag(String assetTag);
    boolean existsByAssetTag(String assetTag);
}
