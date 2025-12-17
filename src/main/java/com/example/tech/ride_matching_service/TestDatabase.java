package com.example.tech.ride_matching_service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.example.tech.ride_matching_service.model.Driver;
import com.example.tech.ride_matching_service.model.Location;
import com.example.tech.ride_matching_service.model.Name;
import com.example.tech.ride_matching_service.model.Rider;
import com.example.tech.ride_matching_service.model.Vehicle;
import com.example.tech.ride_matching_service.model.VehicleModel;
import com.example.tech.ride_matching_service.repositories.DriverRepository;
import com.example.tech.ride_matching_service.repositories.RiderRepository;
import com.example.tech.ride_matching_service.repositories.VehicleModelRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "feature.dev.database", havingValue = "true", matchIfMissing = false)
public class TestDatabase implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        saveDrivers();
        saveRiders();
    }

    private void saveDrivers() {
        Driver driver = new Driver();
        driver.setLocation(Location.of(1D, 2D));
        driver.setName(Name.of("John", "Smith"));
        VehicleModel model = vehicleModelRepository.save(new VehicleModel("Honda Civic", "Grey"));
        driver.setVehicle(new Vehicle(model, "FLY 285"));

        Driver driver2 = new Driver();
        driver2.setLocation(Location.of(1532D, 143232D));
        driver2.setName(Name.of("Alice", "Baker"));
        VehicleModel model2 = vehicleModelRepository.save(new VehicleModel("Toyota Prius", "Blue"));
        driver2.setVehicle(new Vehicle(model2, "BLY 943"));

        List<Driver> drivers = driverRepository.saveAll(List.of(driver, driver2));
        log.debug("{}", drivers);
    }

    private void saveRiders() {
        Rider rider = new Rider(Name.of("Joseph", "Mercieca"));
        Rider rider2 = new Rider(Name.of("Maria", "Buttiġieġ"));
        riderRepository.saveAll(List.of(rider, rider2));
    }
}
