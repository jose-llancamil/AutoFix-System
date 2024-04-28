package com.autofix.repairmanagementsystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bonuses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bonusId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference("vehicle-bonus")
    private VehicleEntity vehicle;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "brand", nullable = true)
    private String brand;

    @Column(name = "description", nullable = false, length = 255)
    private String description;
}