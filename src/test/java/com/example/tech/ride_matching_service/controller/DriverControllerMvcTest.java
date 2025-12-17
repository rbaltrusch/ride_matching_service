package com.example.tech.ride_matching_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.tech.ride_matching_service.repositories.DriverRepository;

import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "feature.dev.database=true")
public class DriverControllerMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DriverRepository driverRepository;

    @BeforeEach
    private void setUp() {
        // set all drivers to available
        driverRepository.findAll().stream()
                .forEach(driver -> driverRepository.updateDriverAvailabilityById(driver.getId(), true));
    }

    @Test
    void testFetchAvailableDrivers() throws Exception {
        String location = """
                    {"latitude": 1039, "longitude": 294393 }
                """;
        mvc.perform(
                post("/api/drivers").contentType(MediaType.APPLICATION_JSON_VALUE).content(location).param("amount", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vehicle.numberPlate", is(equalTo("BLY 943"))))
                .andExpect(jsonPath("$[0].location.longitude", is(equalTo(143232D))));
    }
}
