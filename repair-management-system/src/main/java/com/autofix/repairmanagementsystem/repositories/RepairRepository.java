package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.dto.AverageRepairTimeDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeMotorSummaryDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeSummaryDTO;
import com.autofix.repairmanagementsystem.entities.RepairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<RepairEntity, Long> {
    // Encuentra reparaciones dentro de un rango de fechas
    List<RepairEntity> findByEntryDateBetween(LocalDate start, LocalDate end);

    // Método para encontrar reparaciones por el ID del vehículo
    List<RepairEntity> findByVehicleVehicleId(Long vehicleId);

    // Obtener el costo total de las reparaciones para un vehículo específico
    @Query("SELECT SUM(r.repairCost) FROM RepairEntity r WHERE r.vehicle.vehicleId = :vehicleId")
    Double findTotalRepairCostByVehicleId(Long vehicleId);

    // Obtener el número de reparaciones por vehículo
    @Query("SELECT COUNT(r) FROM RepairEntity r WHERE r.vehicle.vehicleId = :vehicleId AND r.entryDate >= :startDate")
    Long countRepairsByVehicleIdAndDateRange(@Param("vehicleId") Long vehicleId, @Param("startDate") LocalDate startDate);

    @Query("SELECT new com.autofix.repairmanagementsystem.dto.RepairTypeSummaryDTO(rt.description, COUNT(DISTINCT v.type), SUM(r.repairCost)) " +
            "FROM RepairEntity r " +
            "JOIN r.vehicle v " +
            "JOIN r.repairType rt " +
            "GROUP BY rt.description " +
            "ORDER BY SUM(r.repairCost) DESC")
    List<RepairTypeSummaryDTO> findRepairTypesSummary();

    @Query("SELECT new com.autofix.repairmanagementsystem.dto.AverageRepairTimeDTO(v.brand, AVG(TIMESTAMPDIFF(DAY, r.entryDate, r.exitDate))) " +
            "FROM RepairEntity r JOIN r.vehicle v " +
            "GROUP BY v.brand " +
            "ORDER BY AVG(TIMESTAMPDIFF(DAY, r.entryDate, r.exitDate)) ASC")
    List<AverageRepairTimeDTO> findAverageRepairTimesByBrand();

    @Query("SELECT new com.autofix.repairmanagementsystem.dto.RepairTypeMotorSummaryDTO(" +
            "rt.description, v.engineType, COUNT(v), " +
            "CAST(SUM(CASE " +
            "WHEN v.engineType = 'Gasoline' THEN rt.baseCostGasoline " +
            "WHEN v.engineType = 'Diesel' THEN rt.baseCostDiesel " +
            "WHEN v.engineType = 'Hybrid' THEN rt.baseCostHybrid " +
            "WHEN v.engineType = 'Electric' THEN rt.baseCostElectric " +
            "ELSE 0 END) AS Long)) " +
            "FROM RepairEntity r " +
            "JOIN r.vehicle v " +
            "JOIN r.repairType rt " +
            "GROUP BY rt.description, v.engineType " +
            "ORDER BY SUM(CASE " +
            "WHEN v.engineType = 'Gasoline' THEN rt.baseCostGasoline " +
            "WHEN v.engineType = 'Diesel' THEN rt.baseCostDiesel " +
            "WHEN v.engineType = 'Hybrid' THEN rt.baseCostHybrid " +
            "WHEN v.engineType = 'Electric' THEN rt.baseCostElectric " +
            "ELSE 0 END) DESC")
    List<RepairTypeMotorSummaryDTO> findRepairTypesAndEngineSummary();
}