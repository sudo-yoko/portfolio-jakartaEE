package com.example.application;

import java.io.IOException;

import com.example.ApplicationProperties;
import com.example.infrastructure.auth.AuthCache;
import com.example.infrastructure.auth.AuthInfo;
import com.example.infrastructure.auth.AuthResult;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * 認証フィルタ
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private AuthCache authCache;

    @Inject
    private AuthInfo authInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String apiKey = requestContext.getHeaderString("X-Api-Key");
        if (apiKey == null || apiKey.isBlank()) {
            throw new BadRequestException("APIキーが設定されていません。");
        }
        AuthResult result = authCache.authenticate(ApplicationProperties.get("application.name"), apiKey);
        authInfo.setAuthResult(result);
    }
}
