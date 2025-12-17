package com.example.tech.ride_matching_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tech.ride_matching_service.model.Ride;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    @Modifying
    @Query("UPDATE Ride set endTimestamp = instant WHERE id = :rideId")
    void completeOngoingRideByDriverId(long rideId);
}
