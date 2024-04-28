package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.ChargeEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.repositories.ChargeRepository;
import com.autofix.repairmanagementsystem.repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ChargeServiceTest {
    @Mock
    private ChargeRepository chargeRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private ChargeService chargeService;

    private ChargeEntity charge;
    private VehicleEntity vehicle;

    @BeforeEach
    void setUp() {
        charge = new ChargeEntity(1L, "Mileage Overcharge", 200.0, ChargeEntity.ChargeType.MILEAGE, "Sedan");
        vehicle = new VehicleEntity();
        vehicle.setVehicleId(1L);
        vehicle.setMileage(15000);
        vehicle.setType("Sedan");
        vehicle.setManufactureYear(2015);
    }

    @Test
    void createCharge_ShouldSaveCharge() {
        when(chargeRepository.save(any(ChargeEntity.class))).thenReturn(charge);
        ChargeEntity created = chargeService.createCharge(charge);
        assertEquals(charge.getDescription(), created.getDescription());
        verify(chargeRepository).save(charge);
    }

    @Test
    void findAllCharges_ShouldReturnAllCharges() {
        when(chargeRepository.findAll()).thenReturn(Arrays.asList(charge));
        assertFalse(chargeService.findAllCharges().isEmpty());
        verify(chargeRepository).findAll();
    }

    @Test
    void findChargeById_ShouldReturnCharge() {
        when(chargeRepository.findById(1L)).thenReturn(Optional.of(charge));
        assertTrue(chargeService.findChargeById(1L).isPresent());
        verify(chargeRepository).findById(1L);
    }

    @Test
    void updateCharge_ShouldUpdateProperties() {
        ChargeEntity updatedDetails = new ChargeEntity(1L, "Updated Description", 300.0, ChargeEntity.ChargeType.MILEAGE, "SUV");
        when(chargeRepository.findById(1L)).thenReturn(Optional.of(charge));
        when(chargeRepository.save(any(ChargeEntity.class))).thenReturn(updatedDetails);

        ChargeEntity updated = chargeService.updateCharge(1L, updatedDetails);
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(300.0, updated.getAmount());
        verify(chargeRepository).save(charge);
    }

    @Test
    void determineMileageChargePercentage_ShouldCalculateCorrectly() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("7.0"), chargePercentage);
    }

    @Test
    void determineAntiquityChargePercentage_ShouldCalculateCorrectly() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("5.0"), chargePercentage);
    }
}