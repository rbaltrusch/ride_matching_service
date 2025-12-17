package com.example.tech.ride_matching_service.model;

import jakarta.validation.constraints.NotNull;

public record VehicleDto(String numberPlate, String colour, String model) {
    public static VehicleDto of(@NotNull final Vehicle vehicle) {
        return new VehicleDto(vehicle.getNumberPlate(), vehicle.getModel().getColour(), vehicle.getModel().getName());
    }
}
