package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.RepairEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RepairRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RepairRepository repairRepository;

    @Test
    public void whenFindByEntryDateBetween_thenReturnsRepairs() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 12, 31);

        VehicleEntity vehicle = createVehicle();
        entityManager.persist(vehicle);

        RepairTypeEntity repairType = createRepairType();
        entityManager.persist(repairType);

        RepairEntity repair1 = createRepair(vehicle, repairType, startDate);
        entityManager.persist(repair1);

        RepairEntity repair2 = createRepair(vehicle, repairType, endDate);
        entityManager.persist(repair2);

        entityManager.flush();

        var repairs = repairRepository.findByEntryDateBetween(startDate, endDate);
        assertThat(repairs).hasSize(2).extracting(RepairEntity::getRepairId).containsExactlyInAnyOrder(repair1.getRepairId(), repair2.getRepairId());
    }

    @Test
    public void whenFindTotalRepairCostByVehicleId_thenReturnsSum() {
        VehicleEntity vehicle = createVehicle();
        entityManager.persist(vehicle);

        RepairTypeEntity repairType = createRepairType();
        entityManager.persist(repairType);

        entityManager.persist(createRepair(vehicle, repairType, LocalDate.now(), new BigDecimal("100.00")));
        entityManager.persist(createRepair(vehicle, repairType, LocalDate.now(), new BigDecimal("150.00")));
        entityManager.flush();

        Double totalCost = repairRepository.findTotalRepairCostByVehicleId(vehicle.getVehicleId());
        assertThat(totalCost).isEqualTo(250.00);
    }

    @Test
    public void whenCountRepairsByVehicleIdAndDateRange_thenReturnsCount() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        VehicleEntity vehicle = createVehicle();
        entityManager.persist(vehicle);

        RepairTypeEntity repairType = createRepairType();
        entityManager.persist(repairType);

        entityManager.persist(createRepair(vehicle, repairType, LocalDate.of(2022, 1, 15)));
        entityManager.persist(createRepair(vehicle, repairType, LocalDate.of(2022, 2, 15)));
        entityManager.flush();

        Long count = repairRepository.countRepairsByVehicleIdAndDateRange(vehicle.getVehicleId(), startDate);
        assertThat(count).isEqualTo(2);
    }

    private VehicleEntity createVehicle() {
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setEngineType("Gasoline");
        vehicle.setLicensePlateNumber("XYZ123");
        vehicle.setManufactureYear(2020);
        vehicle.setMileage(50000);
        vehicle.setSeatCount(5);
        vehicle.setType("Sedan");
        return vehicle;
    }

    private RepairTypeEntity createRepairType() {
        RepairTypeEntity repairType = new RepairTypeEntity();
        repairType.setDescription("Standard Maintenance");
        repairType.setBaseCostDiesel(new BigDecimal("100.00"));
        repairType.setBaseCostElectric(new BigDecimal("80.00"));
        repairType.setBaseCostGasoline(new BigDecimal("90.00"));
        repairType.setBaseCostHybrid(new BigDecimal("85.00"));
        return repairType;
    }

    private RepairEntity createRepair(VehicleEntity vehicle, RepairTypeEntity repairType, LocalDate entryDate) {
        return createRepair(vehicle, repairType, entryDate, new BigDecimal("120.00"));
    }

    private RepairEntity createRepair(VehicleEntity vehicle, RepairTypeEntity repairType, LocalDate entryDate, BigDecimal repairCost) {
        RepairEntity repair = new RepairEntity();
        repair.setRepairType(repairType);
        repair.setVehicle(vehicle);
        repair.setCustomerPickupDate(LocalDate.now());
        repair.setCustomerPickupTime(LocalTime.now());
        repair.setEntryDate(entryDate);
        repair.setEntryTime(LocalTime.now());
        repair.setExitTime(LocalTime.now().plusHours(1));
        repair.setExitDate(LocalDate.now().plusDays(1));
        repair.setStatus("In Progress");
        repair.setRepairCost(repairCost);
        return repair;
    }
}