package com.example.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger logger = Logger.getLogger(ApplicationExceptionMapper.class.getName());
    private static final String LOG_PREFIX = ">>> [" + ApplicationExceptionMapper.class.getSimpleName() + "]: ";

    @Override
    public Response toResponse(Throwable t) {
        logger.log(Level.SEVERE, LOG_PREFIX + t.getMessage(), t);
        return ErrorResponseFactory.build(t);
    }
}
