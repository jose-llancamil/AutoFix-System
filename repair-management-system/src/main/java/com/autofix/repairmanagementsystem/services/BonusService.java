package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.BonusEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.repositories.BonusRepository;
import com.autofix.repairmanagementsystem.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BonusService {

    private final BonusRepository bonusRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public BonusService(BonusRepository bonusRepository, VehicleRepository vehicleRepository) {
        this.bonusRepository = bonusRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public BonusEntity createBonus(BonusEntity bonus) {
        return bonusRepository.save(bonus);
    }

    public List<BonusEntity> findAllBonuses() {
        return bonusRepository.findAll();
    }

    public Optional<BonusEntity> findBonusById(Long id) {
        return bonusRepository.findById(id);
    }

    @Transactional
    public BonusEntity updateBonus(Long id, BonusEntity bonusDetails) {
        BonusEntity bonus = bonusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bonus no encontrado con el ID: " + id));
        bonus.setBrand(bonusDetails.getBrand());
        bonus.setDescription(bonusDetails.getDescription());
        if (bonusDetails.getAmount() != null) {
            bonus.setAmount(bonusDetails.getAmount());
        }
        return bonusRepository.save(bonus);
    }


    @Transactional
    public void deleteBonus(Long id) {
        bonusRepository.deleteById(id);
    }

    @Transactional
    public BonusEntity applyBonusToVehicle(Long vehicleId, String brand) {
        Optional<VehicleEntity> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            throw new IllegalArgumentException("Vehículo no encontrado con ID: " + vehicleId);
        }

        VehicleEntity vehicle = vehicleOptional.get();
        Optional<BonusEntity> existingBonus = bonusRepository.findByVehicle_VehicleId(vehicleId);
        if (existingBonus.isPresent()) {
            throw new IllegalArgumentException("El vehículo ya tiene un bono aplicado.");
        }

        Optional<BonusEntity> availableBonus = bonusRepository.findFirstByBrandAndVehicleIsNull(brand);
        if (availableBonus.isEmpty()) {
            throw new IllegalArgumentException("No hay bonos disponibles para la marca especificada: " + brand);
        }

        BonusEntity bonus = availableBonus.get();
        bonus.setVehicle(vehicle);
        return bonusRepository.save(bonus);
    }
    public BigDecimal calculateBonusForVehicle(Long vehicleId) {
        Optional<BonusEntity> bonus = bonusRepository.findByVehicle_VehicleId(vehicleId);
        if (bonus.isPresent()) {
            return bonus.get().getAmount(); // Retorna el monto del bono si existe
        } else {
            return BigDecimal.ZERO; // Retorna cero si no hay bono aplicado
        }
    }

}