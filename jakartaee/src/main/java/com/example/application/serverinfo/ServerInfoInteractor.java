package com.example.application.serverinfo;

import java.time.LocalDateTime;

import com.example.domain.services.serverinfo.ServerInfoService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class ServerInfoInteractor {

    @Inject
    private ServerInfoService service;

    public ServerInfoResponse getNow() {
        LocalDateTime now = service.getNow();
        ServerInfoResponse response = new ServerInfoResponse();
        response.setNow(now.toString());
        return response;
    }
}
