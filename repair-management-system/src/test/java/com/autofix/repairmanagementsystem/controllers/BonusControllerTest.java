package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.BonusEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.services.BonusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BonusControllerTest {

    @Mock
    private BonusService bonusService;

    @InjectMocks
    private BonusController bonusController;

    private BonusEntity bonus;
    private VehicleEntity vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new VehicleEntity(
                1L,
                "XYZ123",
                "Toyota",
                "Corolla",
                "Sedan",
                2020,
                "Gasoline",
                15000,
                5,
                new ArrayList<>(),
                new ArrayList<>()
        );

        bonus = new BonusEntity(
                1L,
                vehicle,
                new BigDecimal("500.00"),
                "Toyota",
                "Holiday Special"
        );
    }

    @Test
    void createBonus_ReturnsSavedBonus() {
        when(bonusService.createBonus(any(BonusEntity.class))).thenReturn(bonus);
        ResponseEntity<BonusEntity> response = bonusController.createBonus(bonus);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(bonus);
        verify(bonusService).createBonus(bonus);
    }

    @Test
    void getAllBonuses_ReturnsAllBonuses() {
        when(bonusService.findAllBonuses()).thenReturn(Arrays.asList(bonus));
        ResponseEntity<List<BonusEntity>> response = bonusController.getAllBonuses();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(bonus);
    }

    @Test
    void getBonusById_ReturnsBonus() {
        when(bonusService.findBonusById(1L)).thenReturn(Optional.of(bonus));
        ResponseEntity<BonusEntity> response = bonusController.getBonusById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(bonus);
    }

    @Test
    void getBonusById_ReturnsNotFound() {
        when(bonusService.findBonusById(1L)).thenReturn(Optional.empty());
        ResponseEntity<BonusEntity> response = bonusController.getBonusById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }



    @Test
    void deleteBonus_ReturnsNoContent() {
        doNothing().when(bonusService).deleteBonus(1L);
        ResponseEntity<Void> response = bonusController.deleteBonus(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(bonusService).deleteBonus(1L);
    }

    @Test
    void applyBonusToVehicle_ReturnsAppliedBonus() {
        when(bonusService.applyBonusToVehicle(1L, "Toyota")).thenReturn(bonus);
        ResponseEntity<BonusEntity> response = bonusController.applyBonusToVehicle(1L, "Toyota");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(bonus);
    }

    @Test
    void applyBonusToVehicle_ReturnsBadRequest() {
        when(bonusService.applyBonusToVehicle(1L, "Toyota")).thenThrow(new IllegalArgumentException("Error applying bonus"));
        ResponseEntity<BonusEntity> response = bonusController.applyBonusToVehicle(1L, "Toyota");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }
}