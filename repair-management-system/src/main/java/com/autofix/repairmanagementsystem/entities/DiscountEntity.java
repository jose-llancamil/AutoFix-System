package com.autofix.repairmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column
    private String applicableBrand;

    public enum DiscountType {
        NUM_REPAIRS, // Descuento por número de reparaciones.
        DAY_OF_WEEK, // Descuento por día de la semana.
        LOYALTY, // Descuento por lealtad.
        BONUS // Descuento aplicable por bono.
    }
}