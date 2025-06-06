package com.example.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.development.ReflectionUtils;
import com.example.infrastructure.auth.AuthCache;
import com.example.infrastructure.auth.AuthInfo;
import com.example.infrastructure.auth.InMemoryAuthCache;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;

public class AuthenticationFilterTest1 {

    static AuthenticationFilter filter;

    @BeforeAll
    static void init() {
        AuthCache authCache = new InMemoryAuthCache();
        ReflectionUtils.invokeMethod(authCache, "init");
        AuthInfo authInfo = new AuthInfo();
        filter = new AuthenticationFilter();
        ReflectionUtils.setFieldValue(filter, "authCache", authCache);
        ReflectionUtils.setFieldValue(filter, "authInfo", authInfo);
    }

    // mvn -Dtest=AuthenticationFilterTest1#apiKeyIsBlank test
    @Test
    void apiKeyIsBlank() {
        ContainerRequestContext contextMock = Mockito.mock(ContainerRequestContext.class);
        when(contextMock.getHeaderString(Mockito.eq("X-Api-Key"))).thenReturn("");
        Exception e = assertThrows(BadRequestException.class, () -> filter.filter(contextMock));
        e.printStackTrace();
    }

    // mvn -Dtest=AuthenticationFilterTest1#apiKeyIsInvalid test
    @Test
    void apiKeyIsInvalid() {
        ContainerRequestContext contextMock = Mockito.mock(ContainerRequestContext.class);
        when(contextMock.getHeaderString(Mockito.eq("X-Api-Key"))).thenReturn("invalidKey");
        Exception e = assertThrows(NotAuthorizedException.class, () -> filter.filter(contextMock));
        e.printStackTrace();
    }

    // mvn -Dtest=AuthenticationFilterTest1#success test
    @Test
    void success() {
        try {
            ContainerRequestContext contextMock = Mockito.mock(ContainerRequestContext.class);
            when(contextMock.getHeaderString(Mockito.eq("X-Api-Key"))).thenReturn("key-00001");
            filter.filter(contextMock);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
