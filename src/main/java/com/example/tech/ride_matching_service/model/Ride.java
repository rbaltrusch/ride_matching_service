package com.example.tech.ride_matching_service.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "rides")
@NoArgsConstructor
@RequiredArgsConstructor
public class Ride {

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private long version;

    @Nullable
    @CreationTimestamp
    @Column(insertable = false, updatable = false)
    private Instant startTimestamp;

    @Nullable
    @Column(nullable = true)
    private Instant endTimestamp;

    @NonNull
    @NotNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false, updatable = false)
    private Rider rider; // TODO: support multiple riders

    @NonNull
    @NotNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false, updatable = false)
    private Driver driver;
}
