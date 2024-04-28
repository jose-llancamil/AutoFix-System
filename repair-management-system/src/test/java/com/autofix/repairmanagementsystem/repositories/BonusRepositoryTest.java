package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.BonusEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BonusRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BonusRepository bonusRepository;

    @Test
    public void whenFindFirstByBrandAndVehicleIsNull_thenReturnsBonus() {
        // Preparar datos
        BonusEntity bonus = new BonusEntity();
        bonus.setAmount(new BigDecimal("100.00"));
        bonus.setBrand("Toyota");
        bonus.setDescription("Loyalty Bonus");
        entityManager.persist(bonus);
        entityManager.flush();

        // Ejecutar la consulta
        Optional<BonusEntity> foundBonus = bonusRepository.findFirstByBrandAndVehicleIsNull("Toyota");

        // Validar resultados
        assertThat(foundBonus.isPresent()).isTrue();
        assertThat(foundBonus.get().getBrand()).isEqualTo("Toyota");
        assertThat(foundBonus.get().getVehicle()).isNull();
    }

    @Test
    public void whenFindByVehicle_VehicleId_thenReturnsBonus() {
        // Preparar datos
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setBrand("Honda");
        vehicle.setModel("Civic");
        vehicle.setEngineType("Gasoline");
        vehicle.setLicensePlateNumber("ABC123");
        vehicle.setManufactureYear(2019);
        vehicle.setMileage(30000);
        vehicle.setSeatCount(4);
        vehicle.setType("Sedan");
        entityManager.persist(vehicle);

        BonusEntity bonus = new BonusEntity();
        bonus.setAmount(new BigDecimal("50.00"));
        bonus.setBrand("Honda");
        bonus.setDescription("Referral Bonus");
        bonus.setVehicle(vehicle);
        entityManager.persist(bonus);
        entityManager.flush();

        // Ejecutar la consulta
        Optional<BonusEntity> foundBonus = bonusRepository.findByVehicle_VehicleId(vehicle.getVehicleId());

        // Validar resultados
        assertThat(foundBonus.isPresent()).isTrue();
        assertThat(foundBonus.get().getVehicle().getVehicleId()).isEqualTo(vehicle.getVehicleId());
        assertThat(foundBonus.get().getBrand()).isEqualTo("Honda");
    }
}