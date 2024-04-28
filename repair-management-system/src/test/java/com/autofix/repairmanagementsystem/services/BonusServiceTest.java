package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.BonusEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.repositories.BonusRepository;
import com.autofix.repairmanagementsystem.repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BonusServiceTest {

    @Mock
    private BonusRepository bonusRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private BonusService bonusService;

    private BonusEntity bonus;
    private VehicleEntity vehicle;

    @BeforeEach
    void setUp() {
        bonus = new BonusEntity(1L, null, new BigDecimal("100.00"), "Toyota", "Description of bonus");
        vehicle = new VehicleEntity(1L, "ABC123", "Toyota", "Corolla", "Sedan", 2020, "Gasoline", 15000, 5, null, null);
    }

    @Test
    void createBonus_Success() {
        when(bonusRepository.save(any(BonusEntity.class))).thenReturn(bonus);
        BonusEntity createdBonus = bonusService.createBonus(bonus);
        assertThat(createdBonus).isNotNull();
        assertThat(createdBonus.getAmount()).isEqualTo(new BigDecimal("100.00"));
        verify(bonusRepository).save(bonus);
    }

    @Test
    void findAllBonuses_Success() {
        when(bonusRepository.findAll()).thenReturn(List.of(bonus));
        List<BonusEntity> bonuses = bonusService.findAllBonuses();
        assertThat(bonuses).isNotEmpty();
        assertThat(bonuses.size()).isEqualTo(1);
    }

    @Test
    void findBonusById_NotFound() {
        when(bonusRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<BonusEntity> foundBonus = bonusService.findBonusById(1L);
        assertThat(foundBonus).isNotPresent();
    }

    @Test
    void updateBonus_NotFound() {
        when(bonusRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> bonusService.updateBonus(1L, bonus));
    }

    @Test
    void applyBonusToVehicle_BonusAlreadyApplied() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(bonusRepository.findByVehicle_VehicleId(1L)).thenReturn(Optional.of(bonus));
        assertThrows(IllegalArgumentException.class, () -> bonusService.applyBonusToVehicle(1L, "Toyota"));
    }

    @Test
    void calculateBonusForVehicle_NoBonusFound() {
        when(bonusRepository.findByVehicle_VehicleId(1L)).thenReturn(Optional.empty());
        BigDecimal result = bonusService.calculateBonusForVehicle(1L);
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }
}