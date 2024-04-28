package com.autofix.repairmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairTypeSummaryDTO {
    private String repairType;
    private long vehicleTypeCount;
    private BigDecimal totalCost;
}