package com.autofix.repairmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairCostReportDTO {
    private Long vehicleId;
    private String vehicleDetails;
    private BigDecimal totalCost;
    private int numberOfRepairs;
}