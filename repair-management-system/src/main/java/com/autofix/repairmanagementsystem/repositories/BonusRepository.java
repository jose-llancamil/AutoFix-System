package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.BonusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BonusRepository extends JpaRepository<BonusEntity, Long> {
    Optional<BonusEntity> findFirstByBrandAndVehicleIsNull(String brand);

    Optional<BonusEntity> findByVehicle_VehicleId(Long vehicleId);
}