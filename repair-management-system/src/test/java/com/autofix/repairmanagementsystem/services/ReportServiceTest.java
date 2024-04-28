package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.dto.AverageRepairTimeDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeMotorSummaryDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeSummaryDTO;
import com.autofix.repairmanagementsystem.repositories.RepairRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
}