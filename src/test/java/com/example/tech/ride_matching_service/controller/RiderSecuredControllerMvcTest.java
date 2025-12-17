package com.example.tech.ride_matching_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.tech.ride_matching_service.controller.RiderSecuredController.RideDto;
import com.example.tech.ride_matching_service.model.Driver;
import com.example.tech.ride_matching_service.model.Rider;
import com.example.tech.ride_matching_service.repositories.DriverRepository;
import com.example.tech.ride_matching_service.repositories.RideRepository;
import com.example.tech.ride_matching_service.repositories.RiderRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "feature.dev.database=true")
public class RiderSecuredControllerMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private EntityManager entityManager;

    private Rider rider;
    private Driver expectedDriver;

    @BeforeEach
    private void setUp() {
        rider = riderRepository.findAll().get(0);
        expectedDriver = driverRepository.findAll().stream()
                .filter(x -> x.getVehicle().getNumberPlate().equals("FLY 285")).findFirst().orElseThrow();

        // set all drivers to available
        driverRepository.findAll().stream()
                .forEach(driver -> driverRepository.updateDriverAvailabilityById(driver.getId(), true));
    }

    /** Requests ride, then completes it */
    @Test
    void testFullRide() throws Exception {
        String location = """
                    {"latitude": 105, "longitude": -23434 }
                """;
        String url = "/api/riders/%s".formatted(rider.getId());
        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.driver.vehicle.numberPlate",
                        is(equalTo(expectedDriver.getVehicle().getNumberPlate()))))
                .andDo(result -> {
                    entityManager.flush();
                    rideRepository.flush();
                    ObjectMapper reader = new ObjectMapper();
                    String body = result.getResponse().getContentAsString();
                    RideDto ride = reader.readValue(body, RideDto.class);

                    // assert driver is now unavailable
                    Optional<Driver> driver = driverRepository
                            .findByVehicleNumberPlate(ride.driver().vehicle().numberPlate());
                    assertTrue(driver.isPresent());
                    assertFalse(driver.get().isAvailable());

                    // complete ride
                    mvc.perform(patch("%s/rides/%s".formatted(url, ride.id()))).andExpect(status().isOk());
                });

        // assert driver is available again
        Optional<Driver> driver = driverRepository
                .findByVehicleNumberPlate(expectedDriver.getVehicle().getNumberPlate());
        assertTrue(driver.isPresent());
        entityManager.refresh(driver.get());
        assertTrue(driver.get().isAvailable());
    }
}
