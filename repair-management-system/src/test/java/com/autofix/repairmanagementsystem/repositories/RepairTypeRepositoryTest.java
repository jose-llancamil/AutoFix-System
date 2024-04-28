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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class RepairTypeRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RepairTypeRepository repairTypeRepository;

    @Test
    public void whenCountByRepairTypeId_thenReturnsCount() {
        // Crear y persistir un Vehicle
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setEngineType("Gasoline");
        vehicle.setLicensePlateNumber("XYZ123");
        vehicle.setManufactureYear(2020);
        vehicle.setMileage(50000);
        vehicle.setSeatCount(5);
        vehicle.setType("Sedan");
        testEntityManager.persist(vehicle);

        // Crear y configurar la entidad RepairType
        RepairTypeEntity repairType = new RepairTypeEntity();
        repairType.setDescription("Standard Maintenance");
        repairType.setBaseCostDiesel(new BigDecimal("100.00"));
        repairType.setBaseCostElectric(new BigDecimal("80.00"));
        repairType.setBaseCostGasoline(new BigDecimal("90.00"));
        repairType.setBaseCostHybrid(new BigDecimal("85.00"));
        testEntityManager.persist(repairType);

        // Crear y configurar la entidad RepairEntity
        RepairEntity repair = new RepairEntity();
        repair.setRepairType(repairType);
        repair.setVehicle(vehicle);  // Asignar el Vehicle aquí
        repair.setCustomerPickupDate(LocalDate.now());
        repair.setCustomerPickupTime(LocalTime.now());
        repair.setEntryDate(LocalDate.now());
        repair.setEntryTime(LocalTime.now());
        repair.setExitTime(LocalTime.now().plusHours(1));
        repair.setExitDate(LocalDate.now().plusDays(1));
        repair.setStatus("In Progress");
        repair.setRepairCost(new BigDecimal("150.00"));
        testEntityManager.persist(repair);
        testEntityManager.flush();

        long repairCount = repairTypeRepository.countByRepairTypeId(repairType.getRepairTypeId());
        assertEquals(1, repairCount);
    }
}