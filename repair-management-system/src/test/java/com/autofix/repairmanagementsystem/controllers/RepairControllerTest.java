package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.RepairEntity;
import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.services.RepairService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepairControllerTest {

    @Mock
    private RepairService repairService;

    @InjectMocks
    private RepairController repairController;

    private RepairEntity repair;

    @BeforeEach
    void setUp() {
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setVehicleId(1L);

        RepairTypeEntity repairType = new RepairTypeEntity();
        repairType.setRepairTypeId(1L);

        repair = new RepairEntity();
        repair.setRepairId(1L);
        repair.setVehicle(vehicle);
        repair.setRepairType(repairType);
        repair.setEntryDate(LocalDate.now());
        repair.setEntryTime(LocalTime.of(9, 0));
        repair.setExitDate(LocalDate.now().plusDays(1));
        repair.setExitTime(LocalTime.of(17, 0));
        repair.setCustomerPickupDate(LocalDate.now().plusDays(2));
        repair.setCustomerPickupTime(LocalTime.of(18, 0));
        repair.setRepairCost(new BigDecimal("200.00"));
        repair.setStatus("Completed");
    }

    @Test
    void addRepair_ShouldReturnCreated() throws Exception {
        when(repairService.registerRepair(any(RepairEntity.class))).thenReturn(repair);
        ResponseEntity<RepairEntity> response = repairController.addRepair(repair);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(repair);
        verify(repairService).registerRepair(repair);
    }

    @Test
    void getAllRepairs_ShouldReturnAllRepairs() {
        List<RepairEntity> repairList = new ArrayList<>();
        repairList.add(repair);
        when(repairService.findAllRepairs()).thenReturn(repairList);
        ResponseEntity<List<RepairEntity>> response = repairController.getAllRepairs();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1).contains(repair);
    }

    @Test
    void getRepairById_ShouldReturnRepair() {
        when(repairService.findRepairById(1L)).thenReturn(Optional.of(repair));
        ResponseEntity<RepairEntity> response = repairController.getRepairById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(repair);
    }

    @Test
    void getRepairById_ShouldReturnNotFound() {
        when(repairService.findRepairById(1L)).thenReturn(Optional.empty());
        ResponseEntity<RepairEntity> response = repairController.getRepairById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateRepair_ShouldReturnUpdatedRepair() throws Exception {
        when(repairService.registerRepair(any(RepairEntity.class))).thenReturn(repair);
        ResponseEntity<RepairEntity> response = repairController.updateRepair(1L, repair);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(repair);
    }

    @Test
    void deleteRepair_ShouldReturnNoContent() throws Exception {
        doNothing().when(repairService).deleteRepair(1L);
        ResponseEntity<HttpStatus> response = repairController.deleteRepair(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void getRepairTotalCost_ShouldReturnCost() throws Exception {
        when(repairService.calculateTotalRepairCost(1L)).thenReturn(new BigDecimal("180.00"));
        ResponseEntity<BigDecimal> response = repairController.getRepairTotalCost(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualByComparingTo("180.00");
    }
}