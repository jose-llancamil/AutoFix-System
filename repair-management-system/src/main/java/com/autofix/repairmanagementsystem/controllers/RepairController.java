package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.RepairEntity;
import com.autofix.repairmanagementsystem.services.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/repairs")
@CrossOrigin("*")
public class RepairController {

    private final RepairService repairService;

    @Autowired
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @PostMapping
    public ResponseEntity<RepairEntity> addRepair(@RequestBody RepairEntity repair) {
        try {
            RepairEntity savedRepair = repairService.registerRepair(repair);
            return new ResponseEntity<>(savedRepair, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<RepairEntity>> getAllRepairs() {
        List<RepairEntity> repairs = repairService.findAllRepairs();
        if (repairs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(repairs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairEntity> getRepairById(@PathVariable("id") Long id) {
        try {
            RepairEntity repair = repairService.findRepairById(id)
                    .orElseThrow(() -> new Exception("Repair not found with ID: " + id));
            return new ResponseEntity<>(repair, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairEntity> updateRepair(@PathVariable("id") Long id, @RequestBody RepairEntity repair) {
        try {
            repair.setRepairId(id);
            RepairEntity updatedRepair = repairService.registerRepair(repair);
            return new ResponseEntity<>(updatedRepair, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRepair(@PathVariable("id") Long id) {
        try {
            repairService.deleteRepair(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/total-cost")
    public ResponseEntity<BigDecimal> getRepairTotalCost(@PathVariable("id") Long id) {
        try {
            BigDecimal totalCost = repairService.calculateTotalRepairCost(id);
            return new ResponseEntity<>(totalCost, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}