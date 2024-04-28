package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import com.autofix.repairmanagementsystem.services.RepairTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repair-types")
@CrossOrigin("*")
public class RepairTypeController {

    private final RepairTypeService repairTypeService;

    @Autowired
    public RepairTypeController(RepairTypeService repairTypeService) {
        this.repairTypeService = repairTypeService;
    }

    @PostMapping
    public ResponseEntity<RepairTypeEntity> createRepairType(@RequestBody RepairTypeEntity repairType) {
        try {
            RepairTypeEntity savedRepairType = repairTypeService.createOrUpdateRepairType(repairType);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRepairType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<RepairTypeEntity>> getAllRepairTypes() {
        List<RepairTypeEntity> repairTypes = repairTypeService.findAllRepairTypes();
        return repairTypes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(repairTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairTypeEntity> getRepairTypeById(@PathVariable("id") Long id) {
        return repairTypeService.findRepairTypeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairTypeEntity> updateRepairType(@PathVariable("id") Long id, @RequestBody RepairTypeEntity repairType) {
        try {
            repairType.setRepairTypeId(id);
            RepairTypeEntity updatedRepairType = repairTypeService.createOrUpdateRepairType(repairType);
            return ResponseEntity.ok(updatedRepairType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRepairType(@PathVariable("id") Long id) {
        try {
            repairTypeService.deleteRepairType(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}