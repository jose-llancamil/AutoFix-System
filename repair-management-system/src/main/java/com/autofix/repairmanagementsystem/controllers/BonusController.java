package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.BonusEntity;
import com.autofix.repairmanagementsystem.services.BonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bonuses")
@CrossOrigin("*")
public class BonusController {

    private final BonusService bonusService;

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    public BonusController(BonusService bonusService) {
        this.bonusService = bonusService;
    }

    @PostMapping
    public ResponseEntity<BonusEntity> createBonus(@RequestBody BonusEntity bonus) {
        try {
            BonusEntity savedBonus = bonusService.createBonus(bonus);
            return ResponseEntity.ok(savedBonus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<BonusEntity>> getAllBonuses() {
        List<BonusEntity> bonuses = bonusService.findAllBonuses();
        return bonuses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bonuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BonusEntity> getBonusById(@PathVariable Long id) {
        return bonusService.findBonusById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBonus(@PathVariable Long id, @RequestBody BonusEntity bonus) {
        try {
            BonusEntity updatedBonus = bonusService.updateBonus(id, bonus);
            return ResponseEntity.ok(updatedBonus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Log the exception details to help with debugging
            logger.error("Error updating bonus with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating bonus: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBonus(@PathVariable Long id) {
        try {
            bonusService.deleteBonus(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/apply/{vehicleId}/{brand}")
    public ResponseEntity<BonusEntity> applyBonusToVehicle(@PathVariable Long vehicleId, @PathVariable String brand) {
        try {
            BonusEntity bonus = bonusService.applyBonusToVehicle(vehicleId, brand);
            return ResponseEntity.ok(bonus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}