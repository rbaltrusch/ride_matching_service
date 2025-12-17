package com.example.tech.ride_matching_service.model;

import jakarta.validation.constraints.NotNull;

public record DriverDto(Name name, VehicleDto vehicle, LocationDto location) {
    public static DriverDto of(@NotNull final Driver driver) {
        return new DriverDto(driver.getName(), VehicleDto.of(driver.getVehicle()), LocationDto.of(driver.getLocation()));
    }
}
