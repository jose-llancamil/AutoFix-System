package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.DiscountEntity;
import com.autofix.repairmanagementsystem.repositories.DiscountRepository;
import com.autofix.repairmanagementsystem.repositories.RepairRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceTest {
    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private RepairRepository repairRepository;

    @InjectMocks
    private DiscountService discountService;

    private DiscountEntity discount;

    @BeforeEach
    void setUp() {
        discount = new DiscountEntity(1L, "Spring Special", 10.0, DiscountEntity.DiscountType.NUM_REPAIRS, "Toyota");
    }

    @Test
    void createDiscount_ShouldSaveDiscount() {
        when(discountRepository.save(any(DiscountEntity.class))).thenReturn(discount);
        DiscountEntity created = discountService.createDiscount(discount);
        assertEquals(discount.getDescription(), created.getDescription());
        verify(discountRepository).save(discount);
    }

    @Test
    void findAllDiscounts_ShouldReturnAllDiscounts() {
        when(discountRepository.findAll()).thenReturn(Arrays.asList(discount));
        assertFalse(discountService.findAllDiscounts().isEmpty());
        verify(discountRepository).findAll();
    }

    @Test
    void findDiscountById_ShouldReturnDiscount() {
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));
        assertTrue(discountService.findDiscountById(1L).isPresent());
        verify(discountRepository).findById(1L);
    }

    @Test
    void updateDiscount_ShouldUpdateProperties() {
        DiscountEntity updatedDetails = new DiscountEntity(1L, "Summer Special", 15.0, DiscountEntity.DiscountType.DAY_OF_WEEK, "Honda");
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));
        when(discountRepository.save(any(DiscountEntity.class))).thenReturn(updatedDetails);

        DiscountEntity updated = discountService.updateDiscount(1L, updatedDetails);
        assertEquals("Summer Special", updated.getDescription());
        assertEquals(15.0, updated.getAmount());
        verify(discountRepository).save(discount);
    }

    @Test
    void deleteDiscount_ShouldRemoveDiscount() {
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));
        doNothing().when(discountRepository).delete(discount);
        discountService.deleteDiscount(1L);
        verify(discountRepository).delete(discount);
    }

    @Test
    void determineDiscountPercentage_ShouldCalculateBasedOnEngineTypeAndRepairs() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(3L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "GASOLINE");
        assertEquals(new BigDecimal("10"), discountPercentage);

        discountPercentage = discountService.determineDiscountPercentage(1L, "ELECTRIC");
        assertEquals(new BigDecimal("13"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_GasolineLowRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(1L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "GASOLINE");
        assertEquals(new BigDecimal("5"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_GasolineMidRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(4L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "GASOLINE");
        assertEquals(new BigDecimal("10"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_GasolineHighRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(7L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "GASOLINE");
        assertEquals(new BigDecimal("15"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_GasolineAboveThreshold() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(10L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "GASOLINE");
        assertEquals(new BigDecimal("20"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_DieselLowRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(2L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "DIESEL");
        assertEquals(new BigDecimal("7"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_DieselMidRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(5L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "DIESEL");
        assertEquals(new BigDecimal("12"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_DieselHighRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(9L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "DIESEL");
        assertEquals(new BigDecimal("17"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_DieselAboveThreshold() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(12L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "DIESEL");
        assertEquals(new BigDecimal("22"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_HybridLowRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(1L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "HYBRID");
        assertEquals(new BigDecimal("10"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_HybridMidRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(3L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "HYBRID");
        assertEquals(new BigDecimal("15"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_HybridHighRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(6L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "HYBRID");
        assertEquals(new BigDecimal("20"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_HybridAboveThreshold() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(10L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "HYBRID");
        assertEquals(new BigDecimal("25"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_ElectricLowRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(2L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "ELECTRIC");
        assertEquals(new BigDecimal("8"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_ElectricMidRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(4L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "ELECTRIC");
        assertEquals(new BigDecimal("13"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_ElectricHighRange() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(7L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "ELECTRIC");
        assertEquals(new BigDecimal("18"), discountPercentage);
    }

    @Test
    void determineDiscountPercentage_ElectricAboveThreshold() {
        when(repairRepository.countRepairsByVehicleIdAndDateRange(eq(1L), any(LocalDate.class))).thenReturn(12L);
        BigDecimal discountPercentage = discountService.determineDiscountPercentage(1L, "ELECTRIC");
        assertEquals(new BigDecimal("23"), discountPercentage);
    }
}