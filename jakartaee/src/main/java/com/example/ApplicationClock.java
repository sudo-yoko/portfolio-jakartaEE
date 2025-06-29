package com.example;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ApplicationClock {

    @Inject
    private Clock clock;

    public LocalDateTime now() {
        // 秒以下の精度を切り捨て
        return LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS);
    }
}
