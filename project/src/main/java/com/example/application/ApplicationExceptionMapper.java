package com.example.application;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ApplicationExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toResponse'");
    }

}
