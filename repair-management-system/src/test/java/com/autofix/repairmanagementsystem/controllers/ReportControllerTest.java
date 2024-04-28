package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.dto.AverageRepairTimeDTO;
import com.autofix.repairmanagementsystem.dto.RepairCostReportDTO;
import com.autofix.repairmanagementsystem.dto.RepairTypeSummaryDTO;
import com.autofix.repairmanagementsystem.services.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    public void getRepairCostReport_ReturnsOk_WithContent() throws Exception {
        // Arrange
        when(reportService.generateRepairCostReport()).thenReturn(Arrays.asList(
                new RepairCostReportDTO(1L, "Toyota Camry", new java.math.BigDecimal("5000"), 3)
        ));

        // Act & Assert
        mockMvc.perform(get("/api/v1/reports/repair-costs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].vehicleId").value(1))
                .andExpect(jsonPath("$[0].vehicleDetails").value("Toyota Camry"));
    }

    @Test
    public void getRepairCostReport_ReturnsNoContent_WhenEmpty() throws Exception {
        // Arrange
        when(reportService.generateRepairCostReport()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/reports/repair-costs"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getRepairTypeSummaryReport_ReturnsOk_WithContent() throws Exception {
        // Arrange
        when(reportService.generateRepairTypeSummaryReport()).thenReturn(Arrays.asList(
                new RepairTypeSummaryDTO("General", 10, new java.math.BigDecimal("3000"))
        ));

        // Act & Assert
        mockMvc.perform(get("/api/v1/reports/repair-type-summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].repairType").value("General"));
    }

    @Test
    public void getAverageRepairTimesReport_ReturnsOk_WithContent() throws Exception {
        // Arrange
        when(reportService.generateAverageRepairTimeReport()).thenReturn(Arrays.asList(
                new AverageRepairTimeDTO("Ford", 12.5)
        ));

        // Act & Assert
        mockMvc.perform(get("/api/v1/reports/average-repair-times"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].brand").value("Ford"));
    }
}