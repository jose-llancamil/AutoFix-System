package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.DiscountEntity;
import com.autofix.repairmanagementsystem.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discounts")
@CrossOrigin("*")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping
    public ResponseEntity<DiscountEntity> createDiscount(@RequestBody DiscountEntity discount) {
        try {
            DiscountEntity savedDiscount = discountService.createDiscount(discount);
            return ResponseEntity.ok(savedDiscount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DiscountEntity>> getAllDiscounts() {
        List<DiscountEntity> discounts = discountService.findAllDiscounts();
        return discounts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(discounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountEntity> getDiscountById(@PathVariable Long id) {
        return discountService.findDiscountById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountEntity> updateDiscount(@PathVariable Long id, @RequestBody DiscountEntity discount) {
        try {
            DiscountEntity updatedDiscount = discountService.updateDiscount(id, discount);
            return ResponseEntity.ok(updatedDiscount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        try {
            discountService.deleteDiscount(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}