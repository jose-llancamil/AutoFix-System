import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import repairService from "../services/repair.service";
import vehicleService from "../services/vehicle.service";
import repairTypeService from "../services/repairType.service";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";
import InputLabel from "@mui/material/InputLabel";
import SaveIcon from "@mui/icons-material/Save";
import { DatePicker, TimePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import Dayjs from 'dayjs';

const AddEditRepair = () => {
    const [entryDate, setEntryDate] = useState(null);
    const [exitDate, setExitDate] = useState(null);
    const [entryTime, setEntryTime] = useState(null);
    const [exitTime, setExitTime] = useState(null);
    const [customerPickupDate, setCustomerPickupDate] = useState(null);
    const [customerPickupTime, setCustomerPickupTime] = useState(null);
    const [status, setStatus] = useState("");
    const [repairCost, setRepairCost] = useState("");
    const [repairType, setRepairType] = useState("");
    const [repairTypes, setRepairTypes] = useState([]);
    const [vehicleId, setVehicleId] = useState("");
    const [vehicles, setVehicles] = useState([]);
    const { id } = useParams();
    const navigate = useNavigate();

    const costMatrix = {
        'Gasoline': [120000, 130000, 350000, 210000, 150000, 100000, 100000, 180000, 150000, 130000, 80000],
        'Diesel': [120000, 130000, 450000, 210000, 150000, 120000, 100000, 180000, 150000, 140000, 80000],
        'Hybrid': [180000, 190000, 700000, 300000, 200000, 450000, 100000, 210000, 180000, 220000, 80000],
        'Electric': [220000, 230000, 800000, 300000, 250000, 0, 100000, 250000, 180000, 0, 80000]
    };

    useEffect(() => {
        vehicleService.getAll().then(response => {
            setVehicles(response.data);
        });
        repairTypeService.getAll().then(response => {
            setRepairTypes(response.data);
        });

        if (id) {
            repairService.get(id)
                .then(response => {
                    const repair = response.data;
                    setEntryDate(repair.entryDate ? Dayjs(repair.entryDate) : null);
                    setEntryTime(repair.entryTime ? Dayjs(repair.entryTime, "HH:mm:ss") : null);
                    setExitDate(repair.exitDate ? Dayjs(repair.exitDate) : null);
                    setExitTime(repair.exitTime ? Dayjs(repair.exitTime, "HH:mm:ss") : null);
                    setCustomerPickupDate(repair.customerPickupDate ? Dayjs(repair.customerPickupDate) : null);
                    setCustomerPickupTime(repair.customerPickupTime ? Dayjs(repair.customerPickupTime, "HH:mm:ss") : null);
                    setStatus(repair.status);
                    setRepairCost(repair.repairCost.toString());
                    setRepairType(repair.repairType.repairTypeId);
                    setVehicleId(repair.vehicle.vehicleId);
                })
                .catch(error => {
                    console.error("Error fetching repair details.", error);
                });
        }
    }, [id]);

    useEffect(() => {
        if (vehicleId && repairType) {
            const vehicle = vehicles.find(v => v.vehicleId === vehicleId);
            if (vehicle) {
                const typeIndex = parseInt(repairType) - 1;
                const cost = costMatrix[vehicle.engineType][typeIndex];
                setRepairCost(cost.toString());
            }
        }
    }, [vehicleId, repairType, vehicles]);

    const saveRepair = (e) => {
        e.preventDefault();
        const formattedEntryDate = entryDate ? entryDate.format("YYYY-MM-DD") : null;
        const formattedEntryTime = entryTime ? entryTime.format("HH:mm:ss") : null;
        const formattedExitDate = exitDate ? exitDate.format("YYYY-MM-DD") : null;
        const formattedExitTime = exitTime ? exitTime.format("HH:mm:ss") : null;
        const formattedCustomerPickupDate = customerPickupDate ? customerPickupDate.format("YYYY-MM-DD") : null;
        const formattedCustomerPickupTime = customerPickupTime ? customerPickupTime.format("HH:mm:ss") : null;

        const repairData = {
            entryDate: formattedEntryDate,
            entryTime: formattedEntryTime,
            exitDate: formattedExitDate,
            exitTime: formattedExitTime,
            customerPickupDate: formattedCustomerPickupDate,
            customerPickupTime: formattedCustomerPickupTime,
            status,
            repairCost: parseFloat(repairCost),
            repairType: { repairTypeId: repairType },
            vehicle: { vehicleId }
        };

        const action = id ? repairService.update(id, repairData) : repairService.create(repairData);
        action.then(response => {
            console.log(`${id ? "Updated" : "Added"} repair successfully.`, response.data);
            navigate("/repairs");
        }).catch(error => {
            console.error(`Error ${id ? "updating" : "adding"} repair.`, error);
        });
    };

    return (
        <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center" component="form" onSubmit={saveRepair} sx={{ mt: 4, width: '100%', maxWidth: 600 }}>
            <h3>{id ? "Editar Reparación" : "Agregar Reparación"}</h3>
            <hr />
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker
                    label="Fecha de Entrada"
                    value={entryDate}
                    onChange={setEntryDate}
                    renderInput={(params) => <TextField fullWidth {...params} sx={{ mb: 2 }} />}
                />
                <br />
                <TimePicker
                    label="Hora de Entrada"
                    value={entryTime}
                    onChange={setEntryTime}
                    renderInput={(params) => <TextField fullWidth {...params} sx={{ mb: 2 }} />}
                />
                <br />
                <DatePicker
                    label="Fecha de Salida"
                    value={exitDate}
                    onChange={setExitDate}
                    renderInput={(params) => <TextField fullWidth {...params} sx={{ mb: 2 }} />}
                />
                <br />
                <TimePicker
                    label="Hora de Salida"
                    value={exitTime}
                    onChange={setExitTime}
                    renderInput={(params) => <TextField fullWidth {...params} sx={{ mb: 2 }} />}
                />
                <br />
                <DatePicker
                    label="Fecha de Recogida"
                    value={customerPickupDate}
                    onChange={setCustomerPickupDate}
                    renderInput={(params) => <TextField fullWidth {...params} sx={{ mb: 2 }} />}
                />
                <br />
                <TimePicker
                    label="Hora de Recogida"
                    value={customerPickupTime}
                    onChange={setCustomerPickupTime}
                    renderInput={(params) => <TextField fullWidth {...params} sx={{ mb: 2 }} />}
                />
            </LocalizationProvider>
            <FormControl fullWidth sx={{ mt: 2 }}>
                <TextField label="Estado" value={status} onChange={e => setStatus(e.target.value)} sx={{ mb: 1 }} />
            </FormControl>
            <FormControl fullWidth sx={{ mt: 2 }}>
                <InputLabel id="repair-type-label">Tipo de Reparación</InputLabel>
                <Select
                    labelId="repair-type-label"
                    value={repairType}
                    onChange={e => setRepairType(e.target.value)}
                    label="Tipo de Reparación" fullWidth>
                    {repairTypes.map((type) => (
                        <MenuItem key={type.repairTypeId} value={type.repairTypeId}>
                            {type.description}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            <FormControl fullWidth sx={{ mt: 2 }}>
                <InputLabel id="vehicle-label">Vehículo</InputLabel>
                <Select
                    labelId="vehicle-label"
                    value={vehicleId}
                    onChange={e => setVehicleId(e.target.value)}
                    label="Vehículo" fullWidth>
                    {vehicles.map((vehicle) => (
                        <MenuItem key={vehicle.vehicleId} value={vehicle.vehicleId}>
                            {vehicle.licensePlateNumber}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            <FormControl fullWidth sx={{ mt: 2 }}>
                <TextField label="Costo de Reparación" type="number" value={repairCost} onChange={e => setRepairCost(e.target.value)} sx={{ mb: 1 }} />
            </FormControl>
            <Button variant="contained" color="primary" type="submit" startIcon={<SaveIcon />} sx={{ mt: 2 }}>
                Guardar
            </Button>
            <hr />
        </Box>
    );
};

export default AddEditRepair;