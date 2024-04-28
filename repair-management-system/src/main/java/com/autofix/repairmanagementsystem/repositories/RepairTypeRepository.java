package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairTypeRepository extends JpaRepository<RepairTypeEntity, Long> {

    @Query("SELECT COUNT(r) FROM RepairEntity r WHERE r.repairType.repairTypeId = :repairTypeId")
    long countByRepairTypeId(@Param("repairTypeId") Long repairTypeId);
}