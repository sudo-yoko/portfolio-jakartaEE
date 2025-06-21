package com.example.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.infrastructure.auth.AuthCache;
import com.example.infrastructure.auth.AuthInfo;
import com.example.infrastructure.auth.AuthResult;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
public class AuthenticationFilterTest2 {

    @InjectMocks
    AuthenticationFilter target;
    @Mock
    ContainerRequestContext context;
    @Mock
    AuthCache authCache;
    @Mock
    AuthInfo authInfo;

    // mvn -Dtest=AuthenticationFilterTest2#apiKeyIsBlank test
    @Test
    void apiKeyIsBlank() {
        Mockito.when(context.getHeaderString(Mockito.eq("X-Api-Key"))).thenReturn("");
        Exception e = assertThrows(BadRequestException.class, () -> target.filter(context));
        e.printStackTrace();
    }

    // mvn -Dtest=AuthenticationFilterTest2#apiKeyIsInvalid test
    @Test
    void apiKeyIsInvalid() {
        when(context.getHeaderString(Mockito.eq("X-Api-Key"))).thenReturn("invalidKey");
        when(authCache.authenticate(anyString(), anyString())).thenThrow(
                new NotAuthorizedException("Not Authorized.", Response.status(Response.Status.UNAUTHORIZED).build()));
        Exception e = assertThrows(NotAuthorizedException.class, () -> target.filter(context));
        e.printStackTrace();
    }

    // mvn -Dtest=AuthenticationFilterTest2#success test
    @Test
    void success() {
        try {
            when(context.getHeaderString(Mockito.eq("X-Api-Key"))).thenReturn("key-00001");
            AuthResult authResult = new AuthResult(List.of(), false);
            when(authCache.authenticate(anyString(), anyString())).thenReturn(authResult);
            target.filter(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
