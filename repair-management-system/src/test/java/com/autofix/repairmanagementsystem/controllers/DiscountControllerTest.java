package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.DiscountEntity;
import com.autofix.repairmanagementsystem.services.DiscountService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiscountControllerTest {

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private DiscountController discountController;

    private DiscountEntity discount;

    @BeforeEach
    void setUp() {
        discount = new DiscountEntity(1L, "10% off on services", 10.0, DiscountEntity.DiscountType.LOYALTY, "Toyota");
    }

    @Test
    void createDiscount_ReturnsCreated() {
        when(discountService.createDiscount(any(DiscountEntity.class))).thenReturn(discount);
        ResponseEntity<DiscountEntity> response = discountController.createDiscount(discount);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(discount);
        verify(discountService).createDiscount(discount);
    }

    @Test
    void getAllDiscounts_ReturnsListOfDiscounts() {
        when(discountService.findAllDiscounts()).thenReturn(Arrays.asList(discount));
        ResponseEntity<List<DiscountEntity>> response = discountController.getAllDiscounts();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(discount);
        verify(discountService).findAllDiscounts();
    }

    @Test
    void getDiscountById_ReturnsDiscount() {
        when(discountService.findDiscountById(1L)).thenReturn(Optional.of(discount));
        ResponseEntity<DiscountEntity> response = discountController.getDiscountById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(discount);
        verify(discountService).findDiscountById(1L);
    }

    @Test
    void getDiscountById_ReturnsNotFound() {
        when(discountService.findDiscountById(1L)).thenReturn(Optional.empty());
        ResponseEntity<DiscountEntity> response = discountController.getDiscountById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(discountService).findDiscountById(1L);
    }

    @Test
    void updateDiscount_ReturnsUpdatedDiscount() {
        when(discountService.updateDiscount(eq(1L), any(DiscountEntity.class))).thenReturn(discount);
        ResponseEntity<DiscountEntity> response = discountController.updateDiscount(1L, discount);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(discount);
        verify(discountService).updateDiscount(1L, discount);
    }

    @Test
    void deleteDiscount_ReturnsNoContent() {
        doNothing().when(discountService).deleteDiscount(1L);
        ResponseEntity<Void> response = discountController.deleteDiscount(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(discountService).deleteDiscount(1L);
    }
}