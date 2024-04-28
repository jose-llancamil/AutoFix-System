package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.RepairEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private VehicleEntity vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new VehicleEntity();
        vehicle.setVehicleId(1L);
        vehicle.setLicensePlateNumber("ABC123");
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setType("Sedan");
        vehicle.setManufactureYear(2020);
        vehicle.setEngineType("Gasoline");
        vehicle.setMileage(10000);
        vehicle.setSeatCount(5);
    }

    @Test
    void registerOrUpdateVehicle_WithFutureYear_ThrowsIllegalArgumentException() {
        vehicle.setManufactureYear(Year.now().getValue() + 1);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> vehicleService.registerOrUpdateVehicle(vehicle))
                .withMessageContaining("fuera del rango permitido");
    }

    @Test
    void findAllVehicles_ReturnsList() {
        List<VehicleEntity> vehicles = Collections.singletonList(vehicle);
        when(vehicleRepository.findAll()).thenReturn(vehicles);
        List<VehicleEntity> foundVehicles = vehicleService.findAllVehicles();
        assertThat(foundVehicles).isNotEmpty();
        assertThat(foundVehicles.size()).isEqualTo(1);
    }

    @Test
    void findVehicleById_NotFound_ThrowsException() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> vehicleService.findVehicleById(1L))
                .withMessageContaining("no encontrado");
    }

    @Test
    void deleteVehicle_WithPendingRepairs_ThrowsException() {
        vehicle.setRepairs(List.of(new RepairEntity()));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> vehicleService.deleteVehicle(1L))
                .withMessageContaining("reparaciones pendientes");
    }

    @Test
    void deleteVehicle_Success() throws Exception {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        doNothing().when(vehicleRepository).deleteById(1L);
        vehicleService.deleteVehicle(1L);
        verify(vehicleRepository).deleteById(1L);
    }

    @Test
    void registerOrUpdateVehicle_NonExistentVehicle_ThrowsException() {
        vehicle.setVehicleId(1L);
        when(vehicleRepository.existsById(1L)).thenReturn(false);

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> vehicleService.registerOrUpdateVehicle(vehicle))
                .withMessageContaining("El vehículo con ID 1 no existe.");
    }
}