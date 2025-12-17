package com.example.tech.ride_matching_service.model;

import jakarta.validation.constraints.NotNull;

public record LocationDto(double latitude, double longitude) {
    public static LocationDto of(@NotNull final Location location) {
        return new LocationDto(location.getLatitude(), location.getLongitude());
    }

    public Location convert() {
        return Location.of(latitude, longitude);
    }
}