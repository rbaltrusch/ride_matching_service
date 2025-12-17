package com.example.tech.ride_matching_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tech.ride_matching_service.model.DriverDto;
import com.example.tech.ride_matching_service.model.Location;
import com.example.tech.ride_matching_service.model.LocationDto;
import com.example.tech.ride_matching_service.service.DriverService;
import com.example.tech.ride_matching_service.util.IterTools;

@RestController
@RequestMapping("api/drivers")
public class DriverController {

    @Autowired
    private DriverService service;

    private static final long DEFAULT_DRIVER_AMOUNT = 15;

    /** Get list of currently available drivers */
    @PostMapping
    public Iterable<DriverDto> fetchAvailableDrivers(@RequestBody final LocationDto dto,
            @RequestParam(required = false) final Long amount) {
        long queryAmount = amount == null ? DEFAULT_DRIVER_AMOUNT : amount;
        Location location = dto.convert();
        return IterTools.stream(service.fetchClosestDrivers(location, queryAmount)).map(DriverDto::of)::iterator;
    }
}
