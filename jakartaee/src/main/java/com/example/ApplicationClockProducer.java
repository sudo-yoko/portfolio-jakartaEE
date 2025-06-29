package com.example;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ApplicationClockProducer {
    private static final Logger logger = Logger.getLogger(ApplicationClockProducer.class.getName());
    private static final String LOG_PREFIX = ">>> [" + ApplicationClockProducer.class.getSimpleName() + "]: ";

    private Clock clock;

    @PostConstruct
    private void init() {
        clock = Clock.system(ZoneId.of("Asia/Tokyo"));
        development();
    }

    @Produces
    public Clock produceClock() {
        return clock;
    }

    private void development() {
        // システム日時に仮想日時を設定する
        String fixedClock = ApplicationProperties.getOrDefault("test.clock.fixed", "");
        if (!fixedClock.isBlank()) {
            String[] parts = fixedClock.split(",");
            String instant = parts[0];
            String zoneId = parts[1];
            clock = Clock.fixed(Instant.parse(instant), ZoneId.of(zoneId));
            logger.info(LOG_PREFIX + String.format("clock fixed. [%s %s]", instant, zoneId));
        }
    }
}
