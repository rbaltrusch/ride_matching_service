package com.example.tech.ride_matching_service.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "vehicle_models")
@NoArgsConstructor
@RequiredArgsConstructor
public class VehicleModel {

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @NotNull
    @NotBlank
    private String name;

    @NonNull
    @NotNull
    @NotBlank
    private String colour; // TODO: support localisation

    // TODO: actual vehicle catalogue with pictures included
}
