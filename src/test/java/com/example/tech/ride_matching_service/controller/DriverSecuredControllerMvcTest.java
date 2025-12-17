package com.example.tech.ride_matching_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.tech.ride_matching_service.model.Driver;
import com.example.tech.ride_matching_service.model.Location;
import com.example.tech.ride_matching_service.repositories.DriverRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "feature.dev.database=true")
public class DriverSecuredControllerMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private EntityManager entityManager;

    private Driver driver;

    @BeforeEach
    private void setUp() {
        driver = driverRepository.findAll().get(0);
        driverRepository.updateDriverAvailabilityById(driver.getId(), false);
    }

    @Test
    void testRegisterDriverAsAvailable() throws Exception {
        assertFalse(driver.isAvailable()); // pre-condition: driver is not available

        String url = "/api/drivers/%s/register-available".formatted(driver.getId());
        mvc.perform(patch(url)).andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        // ensure driver is now available
        entityManager.refresh(driver);
        assertTrue(driver.isAvailable());
    }

    @Test
    void testUpdateDriverLocation() throws Exception {
        Location originalLocation = driver.getLocation();
        double newLatitude = originalLocation.getLatitude() + 1;
        double newLongitude = originalLocation.getLongitude() - 102302;
        String location = """
                {"latitude": %s, "longitude": %s }
                """.formatted(newLatitude, newLongitude);

        String url = "/api/drivers/%s/location".formatted(driver.getId());
        mvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(location))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        // ensure driver is now available
        entityManager.refresh(driver);
        assertEquals(newLatitude, driver.getLocation().getLatitude());
        assertEquals(newLongitude, driver.getLocation().getLongitude());
    }
}
