package com.example.tech.ride_matching_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tech.ride_matching_service.model.Rider;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
}
