package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.ChargeEntity;
import com.autofix.repairmanagementsystem.repositories.ChargeRepository;
import com.autofix.repairmanagementsystem.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public ChargeService(ChargeRepository chargeRepository, VehicleRepository vehicleRepository) {
        this.chargeRepository = chargeRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public ChargeEntity createCharge(ChargeEntity charge) {
        return chargeRepository.save(charge);
    }

    public List<ChargeEntity> findAllCharges() {
        return chargeRepository.findAll();
    }

    public Optional<ChargeEntity> findChargeById(Long id) {
        return chargeRepository.findById(id);
    }

    @Transactional
    public ChargeEntity updateCharge(Long id, ChargeEntity chargeDetails) {
        ChargeEntity charge = chargeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carga no encontrada con el ID: " + id));
        charge.setDescription(chargeDetails.getDescription());
        charge.setAmount(chargeDetails.getAmount());
        charge.setChargeType(chargeDetails.getChargeType());
        charge.setApplicableType(chargeDetails.getApplicableType());
        return chargeRepository.save(charge);
    }

    @Transactional
    public void deleteCharge(Long id) {
        chargeRepository.deleteById(id);
    }

    public BigDecimal determineMileageChargePercentage(Long vehicleId) {
        Integer mileage = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con el ID: " + vehicleId))
                .getMileage();
        String vehicleType = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con el ID: " + vehicleId))
                .getType();
        BigDecimal mileageChargePercentage = BigDecimal.ZERO;

        if (vehicleType.equalsIgnoreCase("Sedan")) {
            if (mileage >= 0 && mileage <= 5000) {
                mileageChargePercentage = new BigDecimal("0.0");
            } else if (mileage >= 5001 && mileage <= 12000) {
                mileageChargePercentage = new BigDecimal("3.0");
            } else if (mileage >= 12001 && mileage <= 25000) {
                mileageChargePercentage = new BigDecimal("7.0");
            } else if (mileage >= 25001 && mileage <= 40000) {
                mileageChargePercentage = new BigDecimal("12.0");
            } else if (mileage >= 40000) {
                mileageChargePercentage = new BigDecimal("20.0");
            }

        } else if (vehicleType.equalsIgnoreCase("Hatchback")) {
            if (mileage >= 0 && mileage <= 5000) {
                mileageChargePercentage = new BigDecimal("0.0");
            } else if (mileage >= 5001 && mileage <= 12000) {
                mileageChargePercentage = new BigDecimal("3.0");
            } else if (mileage >= 12001 && mileage <= 25000) {
                mileageChargePercentage = new BigDecimal("7.0");
            } else if (mileage >= 25001 && mileage <= 40000) {
                mileageChargePercentage = new BigDecimal("12.0");
            } else if (mileage >= 40000) {
                mileageChargePercentage = new BigDecimal("20.0");
            }
        } else if (vehicleType.equalsIgnoreCase("SUV")) {
            if (mileage >= 0 && mileage <= 5000) {
                mileageChargePercentage = new BigDecimal("0.0");
            } else if (mileage >= 5001 && mileage <= 12000) {
                mileageChargePercentage = new BigDecimal("5.0");
            } else if (mileage >= 12001 && mileage <= 25000) {
                mileageChargePercentage = new BigDecimal("9.0");
            } else if (mileage >= 25001 && mileage <= 40000) {
                mileageChargePercentage = new BigDecimal("12.0");
            } else if (mileage >= 40000) {
                mileageChargePercentage = new BigDecimal("20.0");
            }
        } else if (vehicleType.equalsIgnoreCase("Pickup")) {
            if (mileage >= 0 && mileage <= 5000) {
                mileageChargePercentage = new BigDecimal("0.0");
            } else if (mileage >= 5001 && mileage <= 12000) {
                mileageChargePercentage = new BigDecimal("5.0");
            } else if (mileage >= 12001 && mileage <= 25000) {
                mileageChargePercentage = new BigDecimal("9.0");
            } else if (mileage >= 25001 && mileage <= 40000) {
                mileageChargePercentage = new BigDecimal("12.0");
            } else if (mileage >= 40000) {
                mileageChargePercentage = new BigDecimal("20.0");
            }
        } else if (vehicleType.equalsIgnoreCase("Furgoneta")) {
            if (mileage >= 0 && mileage <= 5000) {
                mileageChargePercentage = new BigDecimal("0.0");
            } else if (mileage >= 5001 && mileage <= 12000) {
                mileageChargePercentage = new BigDecimal("5.0");
            } else if (mileage >= 12001 && mileage <= 25000) {
                mileageChargePercentage = new BigDecimal("9.0");
            } else if (mileage >= 25001 && mileage <= 40000) {
                mileageChargePercentage = new BigDecimal("12.0");
            } else if (mileage >= 40000) {
                mileageChargePercentage = new BigDecimal("20.0");
            }
        }
        return mileageChargePercentage;
    }

    public BigDecimal determineAntiquityChargePercentage(Long vehicleId) {
        Integer manufactureYear = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con el ID: " + vehicleId))
                .getManufactureYear();
        String vehicleType = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con el ID: " + vehicleId))
                .getType();
        int currentYear = Year.now().getValue();
        int vehicleAge = currentYear - manufactureYear;
        BigDecimal antiquityChargePercentage = BigDecimal.ZERO;

        if (vehicleType.equalsIgnoreCase("Sedan")) {
            if (vehicleAge >= 0 && vehicleAge <= 5) {
                antiquityChargePercentage = new BigDecimal("0.0");
            } else if (vehicleAge >= 6 && vehicleAge <= 10) {
                antiquityChargePercentage = new BigDecimal("5.0");
            } else if (vehicleAge >= 11 && vehicleAge <= 15) {
                antiquityChargePercentage = new BigDecimal("9.0");
            } else if (vehicleAge >= 16) {
                antiquityChargePercentage = new BigDecimal("15.0");
            }
        } else if (vehicleType.equalsIgnoreCase("Hatchback")) {
            if (vehicleAge >= 0 && vehicleAge <= 5) {
                antiquityChargePercentage = new BigDecimal("0.0");
            } else if (vehicleAge >= 6 && vehicleAge <= 10) {
                antiquityChargePercentage = new BigDecimal("5.0");
            } else if (vehicleAge >= 11 && vehicleAge <= 15) {
                antiquityChargePercentage = new BigDecimal("9.0");
            } else if (vehicleAge >= 16) {
                antiquityChargePercentage = new BigDecimal("15.0");
            }
        } else if (vehicleType.equalsIgnoreCase("SUV")) {
            if (vehicleAge >= 0 && vehicleAge <= 5) {
                antiquityChargePercentage = new BigDecimal("0.0");
            } else if (vehicleAge >= 6 && vehicleAge <= 10) {
                antiquityChargePercentage = new BigDecimal("7.0");
            } else if (vehicleAge >= 11 && vehicleAge <= 15) {
                antiquityChargePercentage = new BigDecimal("11.0");
            } else if (vehicleAge >= 16) {
                antiquityChargePercentage = new BigDecimal("20.0");
            }
        } else if (vehicleType.equalsIgnoreCase("Pickup")) {
            if (vehicleAge >= 0 && vehicleAge <= 5) {
                antiquityChargePercentage = new BigDecimal("0.0");
            } else if (vehicleAge >= 6 && vehicleAge <= 10) {
                antiquityChargePercentage = new BigDecimal("7.0");
            } else if (vehicleAge >= 11 && vehicleAge <= 15) {
                antiquityChargePercentage = new BigDecimal("11.0");
            } else if (vehicleAge >= 16) {
                antiquityChargePercentage = new BigDecimal("20.0");
            }
        } else if (vehicleType.equalsIgnoreCase("Furgoneta")) {
            if (vehicleAge >= 0 && vehicleAge <= 5) {
                antiquityChargePercentage = new BigDecimal("0.0");
            } else if (vehicleAge >= 6 && vehicleAge <= 10) {
                antiquityChargePercentage = new BigDecimal("7.0");
            } else if (vehicleAge >= 11 && vehicleAge <= 15) {
                antiquityChargePercentage = new BigDecimal("11.0");
            } else if (vehicleAge >= 16) {
                antiquityChargePercentage = new BigDecimal("20.0");
            }
        }
        return antiquityChargePercentage;
    }
}