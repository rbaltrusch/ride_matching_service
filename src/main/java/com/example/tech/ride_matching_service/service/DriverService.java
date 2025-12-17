package com.example.tech.ride_matching_service.service;

import java.util.Comparator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tech.ride_matching_service.model.Driver;
import com.example.tech.ride_matching_service.model.Location;
import com.example.tech.ride_matching_service.repositories.DriverRepository;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@Service
public class DriverService {

    @Autowired
    private DriverRepository repository;

    public Optional<Driver> fetchDriver(@Nullable final Long driverId) {
        return Optional.ofNullable(driverId).flatMap(repository::findById);
    }

    @Transactional
    public void updateDriverAvailability(@Nullable final Long driverId, final boolean available) {
        // TODO: should throw if driver with given id does not exist?
        if (driverId == null) {
            return;
        }
        repository.updateDriverAvailabilityById(driverId, available);
    }

    @Transactional
    public void updateDriverLocation(@Nullable final Long driverId, @NotNull Location location) {
        if (driverId == null) {
            return;
        }
        repository.updateDriverLocationById(driverId, location);
    }

    // TODO: should use geospatial query
    public Iterable<Driver> fetchClosestDrivers(@NotNull final Location location, long queryAmount) {
        return repository.findAll().stream().sorted(constructLocationComparator(location)).limit(queryAmount).toList();
    }

    private Comparator<Driver> constructLocationComparator(@NotNull final Location location) {
        return (driver1, driver2) -> {
            // Note that we skip the expensive Math.sqrt call and do not compare the actual
            // distances, but merely the squaredDistances, as we are only interested in
            // their relative magnitude. This is a common optimization in distance
            // comparisons.
            return calculateSquaredDistance(location, driver1.getLocation()) > calculateSquaredDistance(location,
                    driver2.getLocation()) ? 1 : -1;
        };
    }

    private double calculateSquaredDistance(@NotNull final Location location, @NotNull final Location destination) {
        double x = Math.pow(location.getLatitude() - destination.getLatitude(), 2);
        double y = Math.pow(location.getLongitude() - destination.getLongitude(), 2);
        return x + y;
    }
}
