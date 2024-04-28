package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public VehicleEntity registerOrUpdateVehicle(VehicleEntity vehicle) throws Exception {
        int currentYear = Year.now().getValue();
        if (vehicle.getManufactureYear() < 1970 || vehicle.getManufactureYear() > currentYear) {
            throw new IllegalArgumentException("El año de fabricación del vehículo está fuera del rango permitido.");
        }
        if (vehicle.getVehicleId() != null) {
            if (!vehicleRepository.existsById(vehicle.getVehicleId())) {
                throw new Exception("El vehículo con ID " + vehicle.getVehicleId() + " no existe.");
            }
        } else {
            Optional<VehicleEntity> existingVehicle = vehicleRepository.findByLicensePlateNumber(vehicle.getLicensePlateNumber());
            if (existingVehicle.isPresent()) {
                throw new Exception("El número de placa ya está registrado: " + vehicle.getLicensePlateNumber());
            }
        }
        return vehicleRepository.save(vehicle);
    }

    public List<VehicleEntity> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    public VehicleEntity findVehicleById(Long vehicleId) throws Exception {
        return vehicleRepository.findById(vehicleId).orElseThrow(() -> new Exception("Vehículo no encontrado con ID: " + vehicleId));
    }

    @Transactional
    public void deleteVehicle(Long vehicleId) throws Exception {
        VehicleEntity vehicle = findVehicleById(vehicleId);
        if (!vehicle.getRepairs().isEmpty()) {
            throw new Exception("El vehículo tiene reparaciones pendientes y no puede ser eliminado.");
        }
        vehicleRepository.deleteById(vehicleId);
    }
}