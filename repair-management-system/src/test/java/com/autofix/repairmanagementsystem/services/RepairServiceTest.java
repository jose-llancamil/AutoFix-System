package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.RepairEntity;
import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.repositories.RepairRepository;
import com.autofix.repairmanagementsystem.repositories.RepairTypeRepository;
import com.autofix.repairmanagementsystem.repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RepairServiceTest {

    @Mock
    private RepairRepository repairRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private RepairTypeRepository repairTypeRepository;
    @Mock
    private DiscountService discountService;
    @Mock
    private ChargeService chargeService;
    @Mock
    private BonusService bonusService;

    @InjectMocks
    private RepairService repairService;

    private RepairEntity repair;

    @BeforeEach
    void setUp() {
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setVehicleId(1L);

        RepairTypeEntity repairType = new RepairTypeEntity();
        repairType.setRepairTypeId(1L);

        repair = new RepairEntity();
        repair.setRepairId(1L);
        repair.setVehicle(vehicle);
        repair.setRepairType(repairType);
        repair.setEntryDate(LocalDate.now());
        repair.setEntryTime(LocalTime.of(10, 0));
        repair.setExitDate(LocalDate.now());
        repair.setExitTime(LocalTime.of(12, 0));
        repair.setRepairCost(new BigDecimal("100.00"));
        repair.setCustomerPickupDate(LocalDate.now().plusDays(1));
        repair.setCustomerPickupTime(LocalTime.of(12, 0));
    }

    @Test
    void registerRepair_ValidatesAndSavesRepair() throws Exception {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(repair.getVehicle()));
        when(repairTypeRepository.findById(anyLong())).thenReturn(Optional.of(repair.getRepairType()));
        when(repairRepository.save(any(RepairEntity.class))).thenReturn(repair);
        RepairEntity savedRepair = repairService.registerRepair(repair);
        assertThat(savedRepair).isNotNull();
        verify(repairRepository).save(repair);
    }

    @Test
    void findAllRepairs_ReturnsListOfRepairs() {
        when(repairRepository.findAll()).thenReturn(Arrays.asList(repair));
        List<RepairEntity> repairs = repairService.findAllRepairs();
        assertThat(repairs).isNotEmpty();
        assertThat(repairs).contains(repair);
    }

    @Test
    void findRepairById_Found_ReturnsRepair() {
        when(repairRepository.findById(1L)).thenReturn(Optional.of(repair));
        Optional<RepairEntity> found = repairService.findRepairById(1L);
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(repair);
    }

    @Test
    void deleteRepair_ValidatesExistenceAndDeletes() throws Exception {
        when(repairRepository.findById(1L)).thenReturn(Optional.of(repair));
        doNothing().when(repairRepository).deleteById(1L);
        repairService.deleteRepair(1L);
        verify(repairRepository).deleteById(1L);
    }

    @Test
    void calculateTotalRepairCost_ComputesCorrectAmount() throws Exception {
        when(repairRepository.findById(1L)).thenReturn(Optional.of(repair));
        when(discountService.determineDiscountPercentage(anyLong(), any())).thenReturn(new BigDecimal("5"));
        when(chargeService.determineMileageChargePercentage(anyLong())).thenReturn(new BigDecimal("3"));
        when(bonusService.calculateBonusForVehicle(anyLong())).thenReturn(new BigDecimal("20"));
        BigDecimal totalCost = repairService.calculateTotalRepairCost(1L);
        assertThat(totalCost).isNotNull();
    }

    @Test
    void findRepairsByVehicleId_ReturnsRepairsList() {
        when(repairRepository.findByVehicleVehicleId(1L)).thenReturn(Arrays.asList(repair));
        List<RepairEntity> foundRepairs = repairService.findRepairsByVehicleId(1L);
        assertThat(foundRepairs).isNotEmpty();
        assertThat(foundRepairs).contains(repair);
    }

    @Test
    void calculateDayOfWeekDiscount_ReturnsDiscount_OnDiscountDaysAndTimes() {
        LocalDate discountDate = LocalDate.of(2024, 4, 11);
        LocalTime discountTime = LocalTime.of(10, 0);
        BigDecimal discount = repairService.calculateDayOfWeekDiscount(discountDate, discountTime);
        assertThat(discount).isEqualTo(new BigDecimal("10"));
    }

    @Test
    void calculatePickupDelayCharge_CalculatesCharge_WhenDelayed() throws Exception {
        when(repairRepository.findById(1L)).thenReturn(Optional.of(repair));
        BigDecimal charge = repairService.calculatePickupDelayCharge(1L);
        assertNotNull(charge);
    }
}