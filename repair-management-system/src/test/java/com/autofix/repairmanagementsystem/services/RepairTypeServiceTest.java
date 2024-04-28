package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import com.autofix.repairmanagementsystem.repositories.RepairTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class RepairTypeServiceTest {

    @Mock
    private RepairTypeRepository repairTypeRepository;

    @InjectMocks
    private RepairTypeService repairTypeService;

    private RepairTypeEntity repairType;

    @BeforeEach
    void setUp() {
        repairType = new RepairTypeEntity();
        repairType.setRepairTypeId(1L);
        repairType.setDescription("General Service");
        repairType.setBaseCostGasoline(new BigDecimal("100.00"));
        repairType.setBaseCostDiesel(new BigDecimal("120.00"));
        repairType.setBaseCostHybrid(new BigDecimal("110.00"));
        repairType.setBaseCostElectric(new BigDecimal("90.00"));
    }

    @Test
    void createOrUpdateRepairType_SavesRepairType() {
        when(repairTypeRepository.save(any(RepairTypeEntity.class))).thenReturn(repairType);
        RepairTypeEntity saved = repairTypeService.createOrUpdateRepairType(repairType);
        assertThat(saved).isEqualTo(repairType);
        verify(repairTypeRepository).save(repairType);
    }

    @Test
    void findAllRepairTypes_ReturnsListOfRepairTypes() {
        when(repairTypeRepository.findAll()).thenReturn(Arrays.asList(repairType));
        List<RepairTypeEntity> repairTypes = repairTypeService.findAllRepairTypes();
        assertThat(repairTypes).isNotEmpty();
        assertThat(repairTypes).contains(repairType);
    }

    @Test
    void findRepairTypeById_Found_ReturnsRepairType() {
        when(repairTypeRepository.findById(1L)).thenReturn(Optional.of(repairType));
        Optional<RepairTypeEntity> found = repairTypeService.findRepairTypeById(1L);
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(repairType);
    }

    @Test
    void deleteRepairType_HasNoRepairs_DeletesRepairType() throws Exception {
        when(repairTypeRepository.countByRepairTypeId(repairType.getRepairTypeId())).thenReturn(0L);
        doNothing().when(repairTypeRepository).deleteById(repairType.getRepairTypeId());
        repairTypeService.deleteRepairType(repairType.getRepairTypeId());
        verify(repairTypeRepository).deleteById(repairType.getRepairTypeId());
    }

    @Test
    void deleteRepairType_HasRepairs_ThrowsException() {
        when(repairTypeRepository.countByRepairTypeId(repairType.getRepairTypeId())).thenReturn(1L);
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> repairTypeService.deleteRepairType(repairType.getRepairTypeId()))
                .withMessageContaining("Existen reparaciones asociadas a este tipo de reparación y no puede ser eliminado.");
    }
}