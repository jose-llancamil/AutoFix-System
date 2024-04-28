import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';  
import bonusService from '../services/bonus.service';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import LoyaltyIcon from '@mui/icons-material/Loyalty';

const BonusList = () => {
    const [bonuses, setBonuses] = useState([]);
    const navigate = useNavigate();  

    useEffect(() => {
        fetchBonuses();
    }, []);

    const fetchBonuses = () => {
        bonusService.getAllBonuses().then(response => {
            console.log("Mostrando listado de todos los bonos.", response.data);
            setBonuses(response.data);
        }).catch(error => {
            console.error('Se ha producido un error al mostrar los bonos.', error);
        });
    };

    const handleDelete = (bonusId) => {
        const confirmDelete = window.confirm("¿Está seguro que desea eliminar este bono?");
        if (confirmDelete) {
            bonusService.deleteBonus(bonusId).then(() => {
                console.log("Bono eliminado.");
                fetchBonuses();
            }).catch(error => {
                console.error('Error al eliminar el bono', error);
            });
        }
    };

    const handleEdit = (bonusId) => {
        navigate(`/bonuses/edit/${bonusId}`); 
    };

    return (
        <TableContainer component={Paper}>
            <br />
            <Link to="/bonuses/create" style={{ textDecoration: "none" }}>
                <div className="card-content">
                    <Button variant="contained" color="primary" startIcon={<LoyaltyIcon />}>
                        Añadir Bono
                    </Button>
                </div>
            </Link>
            <br /><br />
            <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
                <TableHead>
                    <TableRow>
                        <TableCell align="left" sx={{ fontWeight: "bold" }}>Descripción</TableCell>
                        <TableCell align="left" sx={{ fontWeight: "bold" }}>Marca</TableCell>
                        <TableCell align="left" sx={{ fontWeight: "bold" }}>Cantidad</TableCell>
                        <TableCell align="left" sx={{ fontWeight: "bold" }}>Operaciones</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {bonuses.map((bonus) => (
                        <TableRow key={bonus.bonusId}>
                            <TableCell align="left">{bonus.description}</TableCell>
                            <TableCell align="left">{bonus.brand}</TableCell>
                            <TableCell align="left">{bonus.amount}</TableCell>
                            <TableCell>
                                <Button
                                    variant="contained"
                                    color="info"
                                    size="small"
                                    onClick={() => handleEdit(bonus.bonusId)}
                                    startIcon={<EditIcon />}
                                >
                                    Editar
                                </Button>
                                <Button
                                    variant="contained"
                                    color="error"
                                    size="small"
                                    onClick={() => handleDelete(bonus.bonusId)}
                                    startIcon={<DeleteIcon />}
                                    style={{ marginLeft: "10px" }}
                                >
                                    Eliminar
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default BonusList;
