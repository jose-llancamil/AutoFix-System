package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.dto.AverageRepairTimeDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeMotorSummaryDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeSummaryDTO;
import com.autofix.repairmanagementsystem.entities.RepairEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.repositories.RepairRepository;
import com.autofix.repairmanagementsystem.dto.RepairCostReportDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private RepairService repairService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private RepairRepository repairRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateRepairTypeSummaryReport_ReturnsSummaryList() {
        // Arrange
        List<RepairTypeSummaryDTO> expected = Arrays.asList(
                new RepairTypeSummaryDTO("Type1", 10, new BigDecimal("1500.00")),
                new RepairTypeSummaryDTO("Type2", 5, new BigDecimal("750.00"))
        );
        when(repairRepository.findRepairTypesSummary()).thenReturn(expected);

        // Act
        List<RepairTypeSummaryDTO> result = reportService.generateRepairTypeSummaryReport();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void generateAverageRepairTimeReport_ReturnsAverageList() {
        // Arrange
        List<AverageRepairTimeDTO> expected = Arrays.asList(
                new AverageRepairTimeDTO("Brand1", 12.5),
                new AverageRepairTimeDTO("Brand2", 8.0)
        );
        when(repairRepository.findAverageRepairTimesByBrand()).thenReturn(expected);

        // Act
        List<AverageRepairTimeDTO> result = reportService.generateAverageRepairTimeReport();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void generateRepairTypeMotorReport_ReturnsMotorSummaryList() {
        // Arrange
        List<RepairTypeMotorSummaryDTO> expected = Arrays.asList(
                new RepairTypeMotorSummaryDTO("Type1", "V8", 7L, 10L),
                new RepairTypeMotorSummaryDTO("Type2", "V6", 3L, 5L)
        );
        when(repairRepository.findRepairTypesAndEngineSummary()).thenReturn(expected);

        // Act
        List<RepairTypeMotorSummaryDTO> result = reportService.generateRepairTypeMotorReport();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void generateRepairCostReport_ShouldReturnEmptyListWhenNoVehicles() {
        // Arrange
        when(vehicleService.findAllVehicles()).thenReturn(Arrays.asList());

        // Act
        List<RepairCostReportDTO> result = reportService.generateRepairCostReport();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void generateRepairCostReport_ShouldHandleExceptionsInCostCalculation() throws Exception {
        // Arrange
        List<VehicleEntity> vehicles = Arrays.asList(new VehicleEntity());
        vehicles.get(0).setVehicleId(1L);
        vehicles.get(0).setBrand("Brand");
        vehicles.get(0).setModel("Model");

        List<RepairEntity> repairs = Arrays.asList(new RepairEntity());
        repairs.get(0).setRepairId(1L);

        when(vehicleService.findAllVehicles()).thenReturn(vehicles);
        when(repairService.findRepairsByVehicleId(1L)).thenReturn(repairs);
        when(repairService.calculateTotalRepairCost(1L)).thenThrow(new RuntimeException("Error"));

        // Act
        List<RepairCostReportDTO> result = reportService.generateRepairCostReport();

        // Assert
        assertEquals(BigDecimal.ZERO, result.get(0).getTotalCost()); // Assuming you handle the error and set cost to 0
    }

    @Test
    void generateRepairTypeSummaryReport_ShouldReturnEmptyListWhenNoData() {
        // Arrange
        when(repairRepository.findRepairTypesSummary()).thenReturn(Arrays.asList());

        // Act
        List<RepairTypeSummaryDTO> result = reportService.generateRepairTypeSummaryReport();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void generateAverageRepairTimeReport_ShouldReturnEmptyListWhenNoData() {
        // Arrange
        when(repairRepository.findAverageRepairTimesByBrand()).thenReturn(Arrays.asList());

        // Act
        List<AverageRepairTimeDTO> result = reportService.generateAverageRepairTimeReport();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void generateRepairTypeMotorReport_ShouldReturnEmptyListWhenNoData() {
        // Arrange
        when(repairRepository.findRepairTypesAndEngineSummary()).thenReturn(Arrays.asList());

        // Act
        List<RepairTypeMotorSummaryDTO> result = reportService.generateRepairTypeMotorReport();

        // Assert
        assertTrue(result.isEmpty());
    }
}