package com.autofix.repairmanagementsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "repair_types")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairTypeId;

    @Column(nullable = false)
    private String description;

    @Column(name = "base_cost_gasoline", nullable = false)
    private BigDecimal baseCostGasoline;

    @Column(name = "base_cost_diesel", nullable = false)
    private BigDecimal baseCostDiesel;

    @Column(name = "base_cost_hybrid", nullable = false)
    private BigDecimal baseCostHybrid;

    @Column(name = "base_cost_electric", nullable = false)
    private BigDecimal baseCostElectric;
}