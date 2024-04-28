package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.ChargeEntity;
import com.autofix.repairmanagementsystem.services.ChargeService;
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
public class ChargeControllerTest {

    @Mock
    private ChargeService chargeService;

    @InjectMocks
    private ChargeController chargeController;

    private ChargeEntity charge;

    @BeforeEach
    void setUp() {
        charge = new ChargeEntity(1L, "Regular maintenance", 150.00, ChargeEntity.ChargeType.MILEAGE, "Sedan");
    }

    @Test
    void createCharge_ReturnsCreated() {
        when(chargeService.createCharge(any(ChargeEntity.class))).thenReturn(charge);
        ResponseEntity<ChargeEntity> response = chargeController.createCharge(charge);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(charge);
        verify(chargeService).createCharge(charge);
    }

    @Test
    void getAllCharges_ReturnsListOfCharges() {
        when(chargeService.findAllCharges()).thenReturn(Arrays.asList(charge));
        ResponseEntity<List<ChargeEntity>> response = chargeController.getAllCharges();
        assertThat(response.getStatusCode()).isEqualTo(response.getBody().isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
        assertThat(response.getBody()).contains(charge);
        verify(chargeService).findAllCharges();
    }

    @Test
    void getChargeById_ReturnsCharge() {
        when(chargeService.findChargeById(1L)).thenReturn(Optional.of(charge));
        ResponseEntity<ChargeEntity> response = chargeController.getChargeById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(charge);
        verify(chargeService).findChargeById(1L);
    }

    @Test
    void getChargeById_ReturnsNotFound() {
        when(chargeService.findChargeById(1L)).thenReturn(Optional.empty());
        ResponseEntity<ChargeEntity> response = chargeController.getChargeById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(chargeService).findChargeById(1L);
    }

    @Test
    void updateCharge_ReturnsUpdatedCharge() {
        when(chargeService.updateCharge(eq(1L), any(ChargeEntity.class))).thenReturn(charge);
        ResponseEntity<ChargeEntity> response = chargeController.updateCharge(1L, charge);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(charge);
        verify(chargeService).updateCharge(1L, charge);
    }

    @Test
    void deleteCharge_ReturnsNoContent() {
        doNothing().when(chargeService).deleteCharge(1L);
        ResponseEntity<Void> response = chargeController.deleteCharge(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(chargeService).deleteCharge(1L);
    }
}