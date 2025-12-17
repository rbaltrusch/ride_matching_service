package com.example.tech.ride_matching_service.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "drivers")
@NoArgsConstructor
@RequiredArgsConstructor
public class Driver {

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private long version;

    private boolean available;

    @NonNull
    @NotNull
    @Embedded
    private Name name;

    @NonNull
    @NotNull
    @Embedded
    private Vehicle vehicle;

    @NonNull
    @NotNull
    @Embedded
    private Location location;
}
