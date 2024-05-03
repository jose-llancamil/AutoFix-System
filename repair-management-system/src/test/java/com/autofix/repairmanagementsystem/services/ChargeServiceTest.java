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
import java.time.Year;
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

    @Test
    void mileageCharge_ShouldBeZero_WhenMileageIsLowAndTypeIsSUV() {
        vehicle.setType("SUV");
        vehicle.setMileage(5000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("0.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeTwenty_WhenMileageIsHighAndTypeIsPickup() {
        vehicle.setType("Pickup");
        vehicle.setMileage(40001);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("20.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeZero_WhenVehicleIsNewAndTypeIsSUV() {
        vehicle.setType("SUV");
        vehicle.setManufactureYear(Year.now().getValue());
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("0.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeTwenty_WhenVehicleIsOldAndTypeIsPickup() {
        vehicle.setType("Pickup");
        vehicle.setManufactureYear(Year.now().getValue() - 17);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("20.0"), chargePercentage);
    }

    @Test
    void shouldThrowRuntimeException_WhenVehicleNotFoundForMileageCharge() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> chargeService.determineMileageChargePercentage(1L));
    }

    @Test
    void shouldThrowRuntimeException_WhenVehicleNotFoundForAntiquityCharge() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> chargeService.determineAntiquityChargePercentage(1L));
    }

    @Test
    void mileageCharge_ShouldBeThree_WhenMileageIsMediumAndTypeIsHatchback() {
        vehicle.setType("Hatchback");
        vehicle.setMileage(10000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("3.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeTwelve_WhenMileageIsHighAndTypeIsSUV() {
        vehicle.setType("SUV");
        vehicle.setMileage(35000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("12.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeNine_WhenMileageIsModerateAndTypeIsPickup() {
        vehicle.setType("Pickup");
        vehicle.setMileage(13000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("9.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeTwelve_WhenMileageIsHighAndTypeIsFurgoneta() {
        vehicle.setType("Furgoneta");
        vehicle.setMileage(30000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("12.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeFive_WhenVehicleIsModeratelyOldAndTypeIsHatchback() {
        vehicle.setType("Hatchback");
        vehicle.setManufactureYear(Year.now().getValue() - 8);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("5.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeEleven_WhenVehicleIsOldAndTypeIsSUV() {
        vehicle.setType("SUV");
        vehicle.setManufactureYear(Year.now().getValue() - 12);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("11.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeEleven_WhenVehicleIsOldAndTypeIsFurgoneta() {
        vehicle.setType("Furgoneta");
        vehicle.setManufactureYear(Year.now().getValue() - 14);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("11.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeSeven_WhenVehicleIsModeratelyOldAndTypeIsPickup() {
        vehicle.setType("Pickup");
        vehicle.setManufactureYear(Year.now().getValue() - 7);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("7.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeThreeAtUpperBoundaryForSedan() {
        vehicle.setType("Sedan");
        vehicle.setMileage(12000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("3.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeSevenAtLowerBoundaryForSUV() {
        vehicle.setType("SUV");
        vehicle.setMileage(12001);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("9.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeTwelveAtMiddleForPickup() {
        vehicle.setType("Pickup");
        vehicle.setMileage(35000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("12.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeFiveAtUpperBoundaryForHatchback() {
        vehicle.setType("Hatchback");
        vehicle.setManufactureYear(Year.now().getValue() - 10);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("5.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeNineAtLowerBoundaryForSUV() {
        vehicle.setType("SUV");
        vehicle.setManufactureYear(Year.now().getValue() - 11);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("11.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeFifteenAtMiddleAgeForPickup() {
        vehicle.setType("Pickup");
        vehicle.setManufactureYear(Year.now().getValue() - 18);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("20.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeTwentyAtOlderForFurgoneta() {
        vehicle.setType("Furgoneta");
        vehicle.setManufactureYear(Year.now().getValue() - 17);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("20.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeZeroAtLowerBoundaryForSedan() {
        vehicle.setType("Sedan");
        vehicle.setMileage(0);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("0.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeTwelveAtUpperBoundaryForHatchback() {
        vehicle.setType("Hatchback");
        vehicle.setMileage(40000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("12.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeFiveAtUpperBoundaryForSUV() {
        vehicle.setType("SUV");
        vehicle.setMileage(12000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("5.0"), chargePercentage);
    }

    @Test
    void mileageCharge_ShouldBeNineAtUpperBoundaryForPickup() {
        vehicle.setType("Pickup");
        vehicle.setMileage(25000);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineMileageChargePercentage(1L);
        assertEquals(new BigDecimal("9.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeZeroForNewFurgoneta() {
        vehicle.setType("Furgoneta");
        vehicle.setManufactureYear(Year.now().getValue());
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("0.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeSevenForOldSUV() {
        vehicle.setType("SUV");
        vehicle.setManufactureYear(Year.now().getValue() - 10);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("7.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeNineForModeratelyOldSedan() {
        vehicle.setType("Sedan");
        vehicle.setManufactureYear(Year.now().getValue() - 11);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("9.0"), chargePercentage);
    }

    @Test
    void antiquityCharge_ShouldBeFifteenForOldHatchback() {
        vehicle.setType("Hatchback");
        vehicle.setManufactureYear(Year.now().getValue() - 16);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        BigDecimal chargePercentage = chargeService.determineAntiquityChargePercentage(1L);
        assertEquals(new BigDecimal("15.0"), chargePercentage);
    }
}