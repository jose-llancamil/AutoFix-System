package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.ChargeEntity;
import com.autofix.repairmanagementsystem.services.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/charges")
@CrossOrigin("*")
public class ChargeController {

    private final ChargeService chargeService;

    @Autowired
    public ChargeController(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    @PostMapping
    public ResponseEntity<ChargeEntity> createCharge(@RequestBody ChargeEntity charge) {
        try {
            ChargeEntity savedCharge = chargeService.createCharge(charge);
            return ResponseEntity.ok(savedCharge);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ChargeEntity>> getAllCharges() {
        List<ChargeEntity> charges = chargeService.findAllCharges();
        return charges.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(charges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargeEntity> getChargeById(@PathVariable Long id) {
        return chargeService.findChargeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChargeEntity> updateCharge(@PathVariable Long id, @RequestBody ChargeEntity charge) {
        try {
            ChargeEntity updatedCharge = chargeService.updateCharge(id, charge);
            return ResponseEntity.ok(updatedCharge);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharge(@PathVariable Long id) {
        try {
            chargeService.deleteCharge(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}