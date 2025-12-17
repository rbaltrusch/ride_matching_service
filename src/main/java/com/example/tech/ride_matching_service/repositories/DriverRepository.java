package com.example.tech.ride_matching_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tech.ride_matching_service.model.Driver;
import com.example.tech.ride_matching_service.model.Location;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Modifying
    @Query("UPDATE Driver SET available = :available WHERE id = :id")
    void updateDriverAvailabilityById(long id, boolean available);

    @Modifying
    @Query("UPDATE Driver SET location = :location WHERE id = :id")
    void updateDriverLocationById(long id, Location location);

    Optional<Driver> findByVehicleNumberPlate(String numberPlate);
}
