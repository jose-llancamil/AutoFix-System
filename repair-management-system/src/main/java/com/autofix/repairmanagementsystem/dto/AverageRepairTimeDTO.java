package com.autofix.repairmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AverageRepairTimeDTO {
    private String brand;
    private double averageTime;
}