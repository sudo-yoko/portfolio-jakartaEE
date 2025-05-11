package com.example.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger logger = Logger.getLogger(ApplicationExceptionMapper.class.getName());
    private static final String LOG_PREFIX = ">>> [" + ApplicationExceptionMapper.class.getSimpleName() + "]: ";

    @Override
    public Response toResponse(Throwable exception) {
        logger.log(Level.SEVERE, LOG_PREFIX + exception.getMessage(), exception);

        int status = (exception instanceof WebApplicationException)
                ? ((WebApplicationException) exception).getResponse().getStatus()
                : Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String message = exception.getMessage();

        return Response.status(status).entity(message).type(MediaTypes.TEXT_PLAIN_UTF_8).build();
    }

}
