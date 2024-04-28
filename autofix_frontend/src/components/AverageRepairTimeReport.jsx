import React, { useState, useEffect } from 'react';
import reportService from '../services/report.service';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { DataGrid } from '@mui/x-data-grid';

const columns = [
  { field: 'brand', headerName: 'Marca', width: 200 },
  { field: 'averageTime', headerName: 'Tiempo promedio de reparación', width: 300 },
];

const AverageRepairTimeReport = () => {
  const [reportData, setReportData] = useState([]);

  useEffect(() => {
    reportService.generateAverageRepairTimesReport().then((response) => {
      const formattedData = response.data.map((item) => ({
        id: item.brand,
        brand: item.brand,
        averageTime: item.averageTime,
      }));
      setReportData(formattedData);
    }).catch((error) => {
      console.error('Error fetching average repair time report:', error);
    });
  }, []);

  return (
    <Box sx={{ height: 400, width: '100%' }}>
      <Typography variant="h6" gutterBottom component="div" sx={{ mb: 3 }}>
        Informe sobre el tiempo promedio de reparación
      </Typography>
      <Typography variant="body2" gutterBottom>
      Este reporte muestra el tiempo promedio de reparación de cada marca.
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

export default AverageRepairTimeReport;
