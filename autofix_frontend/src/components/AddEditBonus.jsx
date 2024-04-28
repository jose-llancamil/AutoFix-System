import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import bonusService from "../services/bonus.service";
import vehicleService from "../services/vehicle.service";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";
import InputLabel from "@mui/material/InputLabel";
import SaveIcon from "@mui/icons-material/Save";

const AddEditBonus = () => {
    const [bonus, setBonus] = useState({
        brand: "",
        amount: "",
        description: "",  
        vehicle: { vehicleId: "" } 
    });
    const [vehicles, setVehicles] = useState([]);
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        vehicleService.getAll().then(response => {
            setVehicles(response.data);
        });

        if (id) {
            bonusService.getBonus(id).then(response => {
                const data = response.data;
                setBonus({
                    brand: data.brand || "",
                    amount: data.amount || "",
                    description: data.description || "",
                    vehicle: { vehicleId: data.vehicle ? data.vehicle.vehicleId : "" }
                });
            }).catch(error => {
                console.error('Error fetching bonus details:', error);
            });
        } else {
            setBonus({
                brand: "",
                amount: "",
                description: "",
                vehicle: { vehicleId: "" }
            });
        }
    }, [id]);

    const handleChange = (prop, isNested = false) => (event) => {
        if (isNested) {
            setBonus({ ...bonus, vehicle: { ...bonus.vehicle, [prop]: event.target.value } });
        } else {
            setBonus({ ...bonus, [prop]: event.target.value });
        }
    };

    const saveBonus = (e) => {
        e.preventDefault();
        const action = id ? bonusService.updateBonus(id, bonus) : bonusService.createBonus(bonus);
        action.then(() => {
            console.log(`${id ? "Updated" : "Added"} bonus successfully.`);
            navigate("/bonuses");
        }).catch(error => {
            console.error(`Error ${id ? "updating" : "adding"} bonus:`, error);
        });
    };

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            component="form"
            onSubmit={saveBonus}
            sx={{ mt: 4, width: '100%', maxWidth: 600 }}
        >
            <h3>{id ? "Editar Bono" : "Agregar Bono"}</h3>
            <hr />
            <TextField
                label="Marca"
                value={bonus.brand}
                onChange={handleChange("brand")}
                fullWidth
                sx={{ mb: 2 }}
            />
            <TextField
                label="Cantidad"
                type="number"
                value={bonus.amount}
                onChange={handleChange("amount")}
                fullWidth
                sx={{ mb: 2 }}
            />
            <TextField
                label="Descripción"
                value={bonus.description}
                onChange={handleChange("description")}
                fullWidth
                sx={{ mb: 2 }}
            />
            <FormControl fullWidth sx={{ mb: 2 }}>
                <InputLabel id="vehicle-label">Vehículo</InputLabel>
                <Select
                    labelId="vehicle-label"
                    value={bonus.vehicle.vehicleId}
                    onChange={handleChange("vehicleId", true)}
                    label="Vehículo"
                >
                    {vehicles.filter(vehicle => vehicle.brand === bonus.brand).map((vehicle) => (
                        <MenuItem key={vehicle.vehicleId} value={vehicle.vehicleId}>
                            {vehicle.licensePlateNumber} - {vehicle.brand}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            <Button
                variant="contained"
                color="primary"
                type="submit"
                startIcon={<SaveIcon />}
                sx={{ mt: 2 }}
            >
                Guardar
            </Button>
            <hr />
        </Box>
    );
};

export default AddEditBonus;
