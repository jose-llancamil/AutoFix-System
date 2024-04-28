package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.DiscountEntity;
import com.autofix.repairmanagementsystem.repositories.DiscountRepository;
import com.autofix.repairmanagementsystem.repositories.RepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final RepairRepository repairRepository;


    @Autowired
    public DiscountService(DiscountRepository discountRepository, RepairRepository repairRepository) {
        this.discountRepository = discountRepository;
        this.repairRepository = repairRepository;
    }

    @Transactional
    public DiscountEntity createDiscount(DiscountEntity discount) {
        return discountRepository.save(discount);
    }

    public List<DiscountEntity> findAllDiscounts() {
        return discountRepository.findAll();
    }

    public Optional<DiscountEntity> findDiscountById(Long id) {
        return discountRepository.findById(id);
    }

    @Transactional
    public DiscountEntity updateDiscount(Long id, DiscountEntity discountDetails) {
        DiscountEntity discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado con el id: " + id));
        discount.setDescription(discountDetails.getDescription());
        discount.setAmount(discountDetails.getAmount());
        discount.setDiscountType(discountDetails.getDiscountType());
        discount.setApplicableBrand(discountDetails.getApplicableBrand());
        return discountRepository.save(discount);
    }

    @Transactional
    public void deleteDiscount(Long id) {
        DiscountEntity discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado con el id: " + id));
        discountRepository.delete(discount);
    }

    public BigDecimal determineDiscountPercentage(Long vehicleId, String engineType) {
        LocalDate startDate = LocalDate.now().minusMonths(12);
        Long repairCount = repairRepository.countRepairsByVehicleIdAndDateRange(vehicleId, startDate);

        BigDecimal discountPercentage = BigDecimal.ZERO;

        if (engineType.equalsIgnoreCase("GASOLINE")) {
            if (repairCount >= 1 && repairCount <= 2) {
                discountPercentage = new BigDecimal("5");
            } else if (repairCount >= 3 && repairCount <= 5) {
                discountPercentage = new BigDecimal("10");
            } else if (repairCount >= 6 && repairCount <= 9) {
                discountPercentage = new BigDecimal("15");
            } else if (repairCount >= 10) {
                discountPercentage = new BigDecimal("20");
            }
        } else if (engineType.equalsIgnoreCase("DIESEL")) {
            if (repairCount >= 1 && repairCount <= 2) {
                discountPercentage = new BigDecimal("7");
            } else if (repairCount >= 3 && repairCount <= 5) {
                discountPercentage = new BigDecimal("12");
            } else if (repairCount >= 6 && repairCount <= 9) {
                discountPercentage = new BigDecimal("17");
            } else if (repairCount >= 10) {
                discountPercentage = new BigDecimal("22");
            }
        } else if (engineType.equalsIgnoreCase("HYBRID")) {
            if (repairCount >= 1 && repairCount <= 2) {
                discountPercentage = new BigDecimal("10");
            } else if (repairCount >= 3 && repairCount <= 5) {
                discountPercentage = new BigDecimal("15");
            } else if (repairCount >= 6 && repairCount <= 9) {
                discountPercentage = new BigDecimal("20");
            } else if (repairCount >= 10) {
                discountPercentage = new BigDecimal("25");
            }
        } else if (engineType.equalsIgnoreCase("ELECTRIC")) {
            if (repairCount >= 1 && repairCount <= 2) {
                discountPercentage = new BigDecimal("8");
            } else if (repairCount >= 3 && repairCount <= 5) {
                discountPercentage = new BigDecimal("13");
            } else if (repairCount >= 6 && repairCount <= 9) {
                discountPercentage = new BigDecimal("18");
            } else if (repairCount >= 10) {
                discountPercentage = new BigDecimal("23");
            }
        }

        return discountPercentage;
    }
}