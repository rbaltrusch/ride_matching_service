package com.example.tech.ride_matching_service.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @NotNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private VehicleModel model;

    @NotNull
    @NotBlank
    private String numberPlate;
}
