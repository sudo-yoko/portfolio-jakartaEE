package com.example.application.serverinfo;

import java.time.LocalDateTime;

import com.example.domain.services.serverinfo.ServerInfoService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class ServerInfoInteractor {

    @Inject
    private ServerInfoService service;

    public ServerInfoResponse getTime() {
        LocalDateTime time = service.getServerTime();
        ServerInfoResponse response = new ServerInfoResponse();
        response.setServerTime(time.toString());
        return response;
    }
}
