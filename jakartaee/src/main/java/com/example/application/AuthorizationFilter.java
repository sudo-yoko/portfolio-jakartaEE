package com.example.application;

import java.io.IOException;
import java.util.logging.Logger;

import com.example.infrastructure.auth.AuthInfo;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * 認可フィルタ
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    private static final Logger logger = Logger.getLogger(AuthorizationFilter.class.getName());
    private static final String LOG_PREFIX = ">>> [" + AuthorizationFilter.class.getSimpleName() + "]: ";

    @Inject
    private AuthInfo authInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        logger.info(LOG_PREFIX + String.format("Request(inbound) -> method=%s, path=%s", method, path));
        if (!authInfo.hasPermission(method, path)) {
            throw new ForbiddenException("許可されていないエンドポイントです。");
        }
    }
}
