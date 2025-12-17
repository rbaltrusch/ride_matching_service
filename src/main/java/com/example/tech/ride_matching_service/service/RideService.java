package com.example.tech.ride_matching_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tech.ride_matching_service.model.Ride;
import com.example.tech.ride_matching_service.repositories.DriverRepository;
import com.example.tech.ride_matching_service.repositories.RideRepository;
import com.example.tech.ride_matching_service.repositories.RiderRepository;

@Service
public class RideService {

    @Autowired
    private RideRepository repository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Transactional
    public Optional<Ride> completeRide(final Long rideId) {
        if (rideId == null) {
            return Optional.empty();
        }
        repository.completeOngoingRideByDriverId(rideId);
        return repository.findById(rideId);
    }

    @Transactional
    public Optional<Ride> startRide(final Long driverId, final Long riderId) {
        return Optional.ofNullable(riderId)
                .flatMap(riderRepository::findById)
                .flatMap(rider -> Optional.ofNullable(driverId)
                        .flatMap(driverRepository::findById)
                        .map(driver -> repository.save(new Ride(rider, driver))));
    }
}
