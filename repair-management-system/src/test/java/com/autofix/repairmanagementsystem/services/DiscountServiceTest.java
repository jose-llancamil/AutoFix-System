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
}