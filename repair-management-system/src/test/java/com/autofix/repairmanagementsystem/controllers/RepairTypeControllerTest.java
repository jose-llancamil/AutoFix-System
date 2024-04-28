package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import com.autofix.repairmanagementsystem.services.RepairTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepairTypeControllerTest {

    @Mock
    private RepairTypeService repairTypeService;

    @InjectMocks
    private RepairTypeController repairTypeController;

    private RepairTypeEntity repairType;

    @BeforeEach
    void setUp() {
        repairType = new RepairTypeEntity(1L, "General Maintenance", null, null, null, null);
    }

    @Test
    void createRepairType_ShouldReturnCreated() {
        when(repairTypeService.createOrUpdateRepairType(any(RepairTypeEntity.class))).thenReturn(repairType);
        ResponseEntity<RepairTypeEntity> response = repairTypeController.createRepairType(repairType);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(repairType);
        verify(repairTypeService).createOrUpdateRepairType(repairType);
    }

    @Test
    void getAllRepairTypes_ShouldReturnAllRepairTypes() {
        when(repairTypeService.findAllRepairTypes()).thenReturn(Arrays.asList(repairType));
        ResponseEntity<List<RepairTypeEntity>> response = repairTypeController.getAllRepairTypes();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(repairType);
        verify(repairTypeService).findAllRepairTypes();
    }

    @Test
    void getRepairTypeById_ShouldReturnRepairType() {
        when(repairTypeService.findRepairTypeById(1L)).thenReturn(Optional.of(repairType));
        ResponseEntity<RepairTypeEntity> response = repairTypeController.getRepairTypeById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(repairType);
    }

    @Test
    void getRepairTypeById_ShouldReturnNotFound() {
        when(repairTypeService.findRepairTypeById(1L)).thenReturn(Optional.empty());
        ResponseEntity<RepairTypeEntity> response = repairTypeController.getRepairTypeById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateRepairType_ShouldReturnUpdatedRepairType() {
        when(repairTypeService.createOrUpdateRepairType(any(RepairTypeEntity.class))).thenReturn(repairType);
        ResponseEntity<RepairTypeEntity> response = repairTypeController.updateRepairType(1L, repairType);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(repairType);
        verify(repairTypeService).createOrUpdateRepairType(repairType);
    }

    @Test
    void deleteRepairType_ShouldReturnNoContent() throws Exception {
        doNothing().when(repairTypeService).deleteRepairType(1L);
        ResponseEntity<HttpStatus> response = repairTypeController.deleteRepairType(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(repairTypeService).deleteRepairType(1L);
    }

    @Test
    void deleteRepairType_ShouldReturnInternalServerError() throws Exception {
        doThrow(new Exception("Internal server error")).when(repairTypeService).deleteRepairType(1L);
        ResponseEntity<HttpStatus> response = repairTypeController.deleteRepairType(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}