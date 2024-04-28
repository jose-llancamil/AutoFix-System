package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.services.VehicleService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private VehicleEntity vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new VehicleEntity(1L, "ABC123", "Toyota", "Corolla", "Sedan", 2020, "Gasoline", 15000, 5, null, null);
    }

    @Test
    void addVehicle_ShouldReturnCreated() throws Exception {
        when(vehicleService.registerOrUpdateVehicle(any(VehicleEntity.class))).thenReturn(vehicle);
        ResponseEntity<VehicleEntity> response = vehicleController.addVehicle(vehicle);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(vehicle);
        verify(vehicleService).registerOrUpdateVehicle(vehicle);
    }

    @Test
    void getAllVehicles_ShouldReturnVehicleList() {
        when(vehicleService.findAllVehicles()).thenReturn(Arrays.asList(vehicle));
        ResponseEntity<List<VehicleEntity>> response = vehicleController.getAllVehicles();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(vehicle);
        verify(vehicleService).findAllVehicles();
    }

    @Test
    void getVehicleById_ShouldReturnVehicle() throws Exception {
        when(vehicleService.findVehicleById(1L)).thenReturn(vehicle);
        ResponseEntity<VehicleEntity> response = vehicleController.getVehicleById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(vehicle);
        verify(vehicleService).findVehicleById(1L);
    }

    @Test
    void getVehicleById_ShouldReturnNotFound() throws Exception {
        when(vehicleService.findVehicleById(1L)).thenThrow(new Exception("Vehículo no encontrado con ID: " + 1L));
        ResponseEntity<VehicleEntity> response = vehicleController.getVehicleById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(vehicleService).findVehicleById(1L);
    }

    @Test
    void updateVehicle_ShouldReturnUpdatedVehicle() throws Exception {
        when(vehicleService.registerOrUpdateVehicle(any(VehicleEntity.class))).thenReturn(vehicle);
        ResponseEntity<VehicleEntity> response = vehicleController.updateVehicle(1L, vehicle);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(vehicle);
        verify(vehicleService).registerOrUpdateVehicle(vehicle);
    }

    @Test
    void deleteVehicle_ShouldReturnNoContent() throws Exception {
        doNothing().when(vehicleService).deleteVehicle(1L);
        ResponseEntity<HttpStatus> response = vehicleController.deleteVehicle(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(vehicleService).deleteVehicle(1L);
    }
}