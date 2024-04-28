import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import vehicleService from "../services/vehicle.service";
import repairService from "../services/repair.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import BuildIcon from "@mui/icons-material/Build";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogActions from '@mui/material/DialogActions';
import PaidIcon from '@mui/icons-material/Paid';

const RepairList = () => {
  const [vehicles, setVehicles] = useState([]);
  const [open, setOpen] = useState(false);
  const [totalCost, setTotalCost] = useState(0);
  const [selectedRepairId, setSelectedRepairId] = useState(null);
  const navigate = useNavigate();

  const init = () => {
    vehicleService.getAll()
      .then(response => {
        console.log("Mostrando listado de todos los vehículos con reparaciones.", response.data);
        setVehicles(response.data);
      })
      .catch(error => {
        console.log("Error al mostrar los vehículos con reparaciones.", error);
      });
  };

  useEffect(() => {
    init();
  }, []);

  const handleDelete = (repairId) => {
    const confirmDelete = window.confirm("¿Está seguro que desea eliminar esta reparación?");
    if (confirmDelete) {
      repairService.remove(repairId)
        .then(() => {
          console.log("Reparación eliminada.");
          init();
        })
        .catch(error => {
          console.error("Error al eliminar la reparación", error);
        });
    }
  };

  const handleOpenPopup = (repairId) => {
    setSelectedRepairId(repairId);
    repairService.getTotalRepairCost(repairId)
      .then(response => {
        setTotalCost(response.data);
        setOpen(true);
      })
      .catch(error => {
        console.error("Error al obtener el costo total de la reparación", error);
      });
  };

  const handleClosePopup = () => {
    setOpen(false);
    setTotalCost(0);
    setSelectedRepairId(null);
  };

  return (
    <TableContainer component={Paper}>
      <br />
      <Link to="/repairs/create" style={{ textDecoration: "none" }}>
        <div className="card-content">
          <Button variant="contained" color="primary" startIcon={<BuildIcon />}>
            Añadir Reparación
          </Button>
        </div>
      </Link>
      <br /><br />
      <Table sx={{ minWidth: 650 }} aria-label="tabla de reparaciones">
        <TableHead>
          <TableRow>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>Patente</TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>Marca</TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>Modelo</TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>Fecha de Entrada</TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>Tipo de Reparación</TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>Costo Base</TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>Estado</TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>Operaciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {vehicles.map((vehicle) =>
            vehicle.repairs.map((repair) => (
              <TableRow key={repair.repairId}>
                <TableCell align="left">{vehicle.licensePlateNumber}</TableCell>
                <TableCell align="left">{vehicle.brand}</TableCell>
                <TableCell align="left">{vehicle.model}</TableCell>
                <TableCell align="left">{repair.entryDate}</TableCell>
                <TableCell align="left">{repair.repairType.description}</TableCell>
                <TableCell align="left">{repair.repairCost}</TableCell>
                <TableCell align="left">{repair.status}</TableCell>
                <TableCell>
                  <Button
                    variant="contained"
                    color="info"
                    size="small"
                    onClick={() => navigate(`/repairs/edit/${repair.repairId}`)}
                    startIcon={<EditIcon />}
                  >
                    Editar
                  </Button>
                  <Button
                    variant="contained"
                    color="error"
                    size="small"
                    onClick={() => handleDelete(repair.repairId)}
                    startIcon={<DeleteIcon />}
                    style={{ marginLeft: "10px" }}
                  >
                    Eliminar
                  </Button>
                  <Button
                    variant="contained"
                    color="primary"
                    size="small"
                    onClick={() => handleOpenPopup(repair.repairId)}
                    startIcon={<PaidIcon />}
                    style={{ marginLeft: "10px" }}
                  >
                    Costo Total
                  </Button>
                </TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>

      <Dialog open={open} onClose={handleClosePopup}>
        <DialogTitle>Costo Total de la Reparación</DialogTitle>
        <DialogContent>
          <DialogContentText>
            El costo total de la reparación es: {totalCost}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClosePopup} color="primary">
            Cerrar
          </Button>
        </DialogActions>
      </Dialog>
    </TableContainer>
  );
};

export default RepairList;
