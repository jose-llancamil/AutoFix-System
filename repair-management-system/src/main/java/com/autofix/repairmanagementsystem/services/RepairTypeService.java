package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import com.autofix.repairmanagementsystem.repositories.RepairTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RepairTypeService {

    private final RepairTypeRepository repairTypeRepository;

    @Autowired
    public RepairTypeService(RepairTypeRepository repairTypeRepository) {
        this.repairTypeRepository = repairTypeRepository;
    }

    @Transactional
    public RepairTypeEntity createOrUpdateRepairType(RepairTypeEntity repairType) {
        return repairTypeRepository.save(repairType);
    }

    public List<RepairTypeEntity> findAllRepairTypes() {
        return repairTypeRepository.findAll();
    }

    public Optional<RepairTypeEntity> findRepairTypeById(Long repairTypeId) {
        return repairTypeRepository.findById(repairTypeId);
    }

    @Transactional
    public void deleteRepairType(Long repairTypeId) throws Exception {
        // Verificar si hay reparaciones asociadas a este tipo de reparación
        long repairsCount = repairTypeRepository.countByRepairTypeId(repairTypeId);
        if (repairsCount > 0) {
            throw new Exception("Existen reparaciones asociadas a este tipo de reparación y no puede ser eliminado.");
        }
        repairTypeRepository.deleteById(repairTypeId);
    }
}