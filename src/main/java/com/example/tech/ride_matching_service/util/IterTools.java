package com.example.tech.ride_matching_service.util;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IterTools {
    public static <T> Stream<T> stream(@Nullable final Iterable<T> iterable) {
        if (iterable == null) {
            return Stream.empty();
        }
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
