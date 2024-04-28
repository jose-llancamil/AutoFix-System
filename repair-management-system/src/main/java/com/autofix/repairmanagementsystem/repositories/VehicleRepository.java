package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    // Buscar vehículos por número de placa
    Optional<VehicleEntity> findByLicensePlateNumber(String licensePlateNumber);
}