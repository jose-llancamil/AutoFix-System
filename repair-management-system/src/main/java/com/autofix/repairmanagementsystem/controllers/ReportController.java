package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.dto.AverageRepairTimeDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeMotorSummaryDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeSummaryDTO;
import com.autofix.repairmanagementsystem.services.ReportService;
import com.autofix.repairmanagementsystem.dto.RepairCostReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin("*")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/repair-costs")
    public ResponseEntity<List<RepairCostReportDTO>> getRepairCostReport() {
        try {
            List<RepairCostReportDTO> reports = reportService.generateRepairCostReport();
            if (reports.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            System.err.println("Error retrieving repair cost reports: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/repair-type-summary")
    public ResponseEntity<List<RepairTypeSummaryDTO>> getRepairTypeSummaryReport() {
        try {
            List<RepairTypeSummaryDTO> reports = reportService.generateRepairTypeSummaryReport();
            if (reports.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            System.err.println("Error retrieving repair type summary reports: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/average-repair-times")
    public ResponseEntity<List<AverageRepairTimeDTO>> getAverageRepairTimesReport() {
        List<AverageRepairTimeDTO> report = reportService.generateAverageRepairTimeReport();
        if (report.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(report);
    }

    @GetMapping("/repair-types-engine-summary")
    public ResponseEntity<List<RepairTypeMotorSummaryDTO>> getRepairTypesEngineSummary() {
        List<RepairTypeMotorSummaryDTO> report = reportService.generateRepairTypeMotorReport();
        if (report.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(report);
    }
}