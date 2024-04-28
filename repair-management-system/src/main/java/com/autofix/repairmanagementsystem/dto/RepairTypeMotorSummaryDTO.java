package com.autofix.repairmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RepairTypeMotorSummaryDTO {
    private String repairTypeDescription;
    private String engineType;
    private Long vehicleCount;
    private Long totalCost;
}