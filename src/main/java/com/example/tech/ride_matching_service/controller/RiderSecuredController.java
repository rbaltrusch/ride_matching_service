package com.example.tech.ride_matching_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.tech.ride_matching_service.model.Driver;
import com.example.tech.ride_matching_service.model.DriverDto;
import com.example.tech.ride_matching_service.model.LocationDto;
import com.example.tech.ride_matching_service.model.Ride;
import com.example.tech.ride_matching_service.service.DriverService;
import com.example.tech.ride_matching_service.service.RideService;
import com.example.tech.ride_matching_service.util.IterTools;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Note: these endpoints should only be accessible by the respective rider
@RestController
@RequestMapping("api/riders/{id}")
public class RiderSecuredController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private RideService rideService;

    // TODO: should return HTTP 404 if rider cannot be found
    /** Request a ride */
    @PostMapping
    @Transactional
    public Optional<RideDto> requestRide(@PathVariable final Long id, @RequestBody final LocationDto dto) {
        Optional<Driver> driver = IterTools.stream(driverService.fetchClosestDrivers(dto.convert(), 1)).findFirst();
        Long driverId = driver.map(x -> x.getId()).orElse(null);
        driverService.updateDriverAvailability(id, false);
        return rideService.startRide(driverId, id).map(RideDto::of);
    }

    // TODO: should return HTTP 404 if rider or ride cannot be found
    // TODO: should respond with BAD_REQUEST if ride is not ongoing.
    /** Complete the ride */
    @Transactional
    @PatchMapping("rides/{rideId}")
    public ResponseEntity<RideDto> completeRide(@PathVariable final Long id, @PathVariable final Long rideId) {
        Optional<Ride> ride = rideService.completeRide(rideId);
        ride.map(x -> x.getDriver().getId())
                .ifPresent(driverId -> driverService.updateDriverAvailability(driverId, true));
        return ResponseEntity.of(ride.map(RideDto::of));
    }

    public static record RideDto(long id, DriverDto driver) {
        public static RideDto of(@NotNull final Ride ride) {
            return new RideDto(ride.getId(), DriverDto.of(ride.getDriver()));
        }
    }
}
