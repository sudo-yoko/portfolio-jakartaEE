package com.example.infrastructure.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Properties;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;

/**
 * APIクライアント認証：認証認可の定義を読み込み、インメモリで保持する簡易実装
 */
@Singleton
@Startup
public class InMemoryAuthCache implements AuthCache {
    private final Map<String, Map<String, List<Permission>>> permissionMap = new HashMap<>();
    private final String APPLICATION_NAME = Properties.get("application.name");

    @PostConstruct
    private void init() {
        List<Permission> permissions = List.of(
                new Permission("GET", "users/"),
                new Permission("POST", "users/"));
        permissionMap.put("key-00001", Map.of(APPLICATION_NAME, permissions));
    }

    @Override
    public AuthResult authenticate(String applicationName, String apiKey) throws NotAuthorizedException {
        Map<String, List<Permission>> permissions = permissionMap.get(apiKey);
        if (permissions == null || !permissions.containsKey(applicationName)) {
            Response response = Response.status(Response.Status.UNAUTHORIZED).build();
            throw new NotAuthorizedException("Not Authorized.", response);
        }
        return new AuthResult(permissions.get(applicationName), false);
    }
}