package com.example.domain.services.serverinfo;

import java.time.LocalDateTime;

import com.example.ApplicationClock;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ServerInfoService {

    @Inject
    private ApplicationClock clock;

    public LocalDateTime getNow() {
        return clock.now();
    }
}
