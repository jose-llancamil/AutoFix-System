package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class VehicleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    public void whenFindByLicensePlateNumber_thenReturnVehicle() {
        // given
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setLicensePlateNumber("ABC123");
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setType("Sedan");
        vehicle.setManufactureYear(2021);
        vehicle.setEngineType("Gasoline");
        vehicle.setMileage(10000);
        vehicle.setSeatCount(5);
        entityManager.persist(vehicle);
        entityManager.flush();

        // when
        Optional<VehicleEntity> found = vehicleRepository.findByLicensePlateNumber(vehicle.getLicensePlateNumber());

        // then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getLicensePlateNumber()).isEqualTo(vehicle.getLicensePlateNumber());
    }
}