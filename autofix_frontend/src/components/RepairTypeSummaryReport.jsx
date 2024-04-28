import React, { useState, useEffect } from 'react';
import reportService from '../services/report.service';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { DataGrid } from '@mui/x-data-grid';

const columns = [
  { field: 'repairType', headerName: 'Tipo de Reparación', width: 200 },
  { field: 'totalCost', headerName: 'Costo Total', width: 130 },
  { field: 'vehicleTypeCount', headerName: 'Vehículos', width: 180 },
];

const RepairTypeSummaryReport = () => {
  const [reportData, setReportData] = useState([]);

  useEffect(() => {
    reportService.generateRepairTypeSummaryReport().then((response) => {
      const formattedData = response.data.map((item) => ({
        id: item.repairType,
        repairType: item.repairType,
        totalCost: item.totalCost,
        vehicleTypeCount: item.vehicleTypeCount,
      }));
      setReportData(formattedData);
    }).catch((error) => {
      console.error('Error fetching repair type summary report:', error);
    });
  }, []);

  return (
    <Box sx={{ height: 400, width: '100%' }}>
      <Typography variant="h6" gutterBottom component="div" sx={{ mb: 3 }}>
        Informe sobre tipos de reparación
      </Typography>
      <Typography variant="body2" gutterBottom>
        Este informe muestra los costes totales de reparación y el número de vehículos para cada tipo de reparación.
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

export default RepairTypeSummaryReport;
