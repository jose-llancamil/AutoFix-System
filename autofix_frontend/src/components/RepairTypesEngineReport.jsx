import React, { useState, useEffect } from 'react';
import reportService from '../services/report.service';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { DataGrid } from '@mui/x-data-grid';

const columns = [
  { field: 'repairTypeDescription', headerName: 'Tipo de Reparación', width: 200 },
  { field: 'engineType', headerName: 'Tipo de Motor', width: 200 },
  { field: 'vehicleCount', headerName: 'Vehículos', width: 130 },
  { field: 'totalCost', headerName: 'Costo Total', width: 130 },
];

const RepairTypesEngineReport = () => {
  const [reportData, setReportData] = useState([]);

  useEffect(() => {
    reportService.generateRepairTypesEngineSummary().then((response) => {
      const formattedData = response.data.map((item, index) => ({
        id: index + 1,
        repairTypeDescription: item.repairTypeDescription,
        engineType: item.engineType,
        vehicleCount: item.vehicleCount,
        totalCost: item.totalCost,
      }));
      setReportData(formattedData);
    }).catch((error) => {
      console.error('Error fetching repair type motor report:', error);
    });
  }, []);

  return (
    <Box sx={{ height: 400, width: '100%' }}>
      <Typography variant="h6" gutterBottom component="div" sx={{ mb: 3 }}>
        Informe de reparaciones por tipo de motor.
      </Typography>
      <Typography variant="body2" gutterBottom>
        Este reporte muestra el recuento de cada tipo de reparación categorizado por tipo de motor.
      </Typography>
      <DataGrid
        rows={reportData}
        columns={columns}
        pageSize={5}
        rowsPerPageOptions={[5]}
        checkboxSelection
      />
    </Box>
  );
};

export default RepairTypesEngineReport;
