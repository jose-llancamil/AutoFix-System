package com.autofix.repairmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RepairTypeAverageCostDto {

    private String description;
    private BigDecimal averageCost;
}