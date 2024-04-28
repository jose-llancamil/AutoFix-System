package com.autofix.repairmanagementsystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "repairs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @JsonBackReference("vehicle-repair")
    private VehicleEntity vehicle;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "status")
    private String status;

    @Column(name = "entry_time", nullable = false)
    private LocalTime entryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_type_id", nullable = false)
    private RepairTypeEntity repairType;

    @Column(name = "repair_cost", nullable = false)
    private BigDecimal repairCost;

    @Column(name = "exit_date", nullable = true)
    private LocalDate exitDate;

    @Column(name = "exit_time", nullable = true)
    private LocalTime exitTime;

    @Column(name = "customer_pickup_date", nullable = true)
    private LocalDate customerPickupDate;

    @Column(name = "customer_pickup_time", nullable = true)
    private LocalTime customerPickupTime;
}