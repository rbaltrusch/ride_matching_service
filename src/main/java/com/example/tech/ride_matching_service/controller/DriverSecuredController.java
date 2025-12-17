package com.example.tech.ride_matching_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tech.ride_matching_service.model.LocationDto;
import com.example.tech.ride_matching_service.service.DriverService;

import jakarta.validation.constraints.NotNull;

// Note: these endpoints should only be accessible by the respective driver
@RestController
@RequestMapping("api/drivers/{id}")
public class DriverSecuredController {

    @Autowired
    private DriverService service;

    /** Register driver availability */
    @PatchMapping("register-available")
    private ResponseEntity<Void> registerDriverAsAvailable(@PathVariable final Long id) {
        if (service.fetchDriver(id).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        service.updateDriverAvailability(id, true);
        return ResponseEntity.noContent().build();
    }

    // TODO: should return HTTP 404 if driver cannot be found
    /** Register driver availability */
    @PatchMapping("location")
    private ResponseEntity<Void> updateDriverLocation(@PathVariable final Long id, @RequestBody LocationDto location) {
        if (service.fetchDriver(id).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        service.updateDriverLocation(id, location.convert());
        return ResponseEntity.noContent().build();
    }

    public static record RidePostDto(@NotNull Long riderId) {
    }
}
