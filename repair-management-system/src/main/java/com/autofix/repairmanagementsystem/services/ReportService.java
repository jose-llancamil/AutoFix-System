package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.dto.AverageRepairTimeDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeMotorSummaryDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeSummaryDTO;
import com.autofix.repairmanagementsystem.entities.RepairEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.dto.RepairCostReportDTO;
import com.autofix.repairmanagementsystem.repositories.RepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private RepairService repairService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RepairRepository repairRepository;

    public List<RepairCostReportDTO> generateRepairCostReport() {
        List<VehicleEntity> vehicles = vehicleService.findAllVehicles();
        List<RepairCostReportDTO> reports = new ArrayList<>();
        for (VehicleEntity vehicle : vehicles) {
            List<RepairEntity> repairs = repairService.findRepairsByVehicleId(vehicle.getVehicleId());
            BigDecimal totalCost = calculateTotalCost(repairs);
            reports.add(new RepairCostReportDTO(
                    vehicle.getVehicleId(),
                    vehicle.getBrand() + " " + vehicle.getModel(),
                    totalCost,
                    repairs.size()
            ));
        }
        return reports;
    }

    private BigDecimal calculateTotalCost(List<RepairEntity> repairs) {
        BigDecimal total = BigDecimal.ZERO;
        for (RepairEntity repair : repairs) {
            try {
                BigDecimal repairCost = repairService.calculateTotalRepairCost(repair.getRepairId());
                total = total.add(repairCost);
            } catch (Exception e) {
                System.err.println("Error calculating repair cost for repair ID " + repair.getRepairId() + ": " + e.getMessage());
            }
        }
        return total;
    }

    public List<RepairTypeSummaryDTO> generateRepairTypeSummaryReport() {
        return repairRepository.findRepairTypesSummary();
    }

    public List<AverageRepairTimeDTO> generateAverageRepairTimeReport() {
        return repairRepository.findAverageRepairTimesByBrand();
    }

    public List<RepairTypeMotorSummaryDTO> generateRepairTypeMotorReport() {
        return repairRepository.findRepairTypesAndEngineSummary();
    }
}