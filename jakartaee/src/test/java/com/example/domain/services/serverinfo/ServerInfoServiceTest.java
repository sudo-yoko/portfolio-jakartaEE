package com.example.domain.services.serverinfo;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import com.example.ApplicationClock;
import com.example.development.ReflectionUtils;

public class ServerInfoServiceTest {

    // mvn -Dtest=ServerInfoServiceTest#testGetServerDateTime test
    @Test
    void testGetServerDateTime() {
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-02T00:00:00Z"), ZoneId.of("UTC"));

        ApplicationClock appClock = new ApplicationClock();
        ReflectionUtils.setFieldValue(appClock, "clock", fixedClock);

        ServerInfoService target = new ServerInfoService();
        ReflectionUtils.setFieldValue(target, "clock", appClock);

        LocalDateTime result = target.getNow();
        System.out.println(">>> result -> " + result);
    }
}
