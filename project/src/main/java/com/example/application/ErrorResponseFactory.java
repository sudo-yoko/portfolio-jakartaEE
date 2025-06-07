package com.example.application;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.example.ValidationErrorException;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

public class ErrorResponseFactory {
    private static final Logger logger = Logger.getLogger(ErrorResponseFactory.class.getName());
    private static final String LOG_PREFIX = ">>> [" + ErrorResponseFactory.class.getSimpleName() + "]: ";

    private ErrorResponseFactory() {

    }

    public static Response build(Throwable throwable) {
        Throwable cause = findCause(throwable);

        ResponseBuilder builder;
        if (cause instanceof WebApplicationException) {
            builder = handleWebApplicationException((WebApplicationException) cause);
        } else if (cause instanceof ValidationErrorException) {
            builder = handleValidationErrorException((ValidationErrorException) cause);
        } else {
            builder = handleOtherException(cause);
        }
        return builder.type(MediaTypes.APPLICATION_JSON_UTF_8).build();
    }

    public static Throwable findCause(Throwable throwable) {
        final int max_depth = 10;

        Throwable cause = throwable;
        for (int i = 0; i < max_depth; i++) {
            if (cause.getCause() == null) {
                return cause;
            }
            cause = cause.getCause();
        }
        logger.warning(LOG_PREFIX + "原因例外の取得で、ループの最大回数を超えました。");
        return cause;
    }

    public static ResponseBuilder handleWebApplicationException(WebApplicationException cause) {
        int status = cause.getResponse().getStatus();

        ErrorResponse entity = new ErrorResponse();
        entity.setType(cause.getClass().getSimpleName());
        entity.setMessage(cause.getMessage());
        return Response.status(status).entity(entity);
    }

    public static ResponseBuilder handleValidationErrorException(ValidationErrorException cause) {
        int status = Response.Status.BAD_REQUEST.getStatusCode();

        ErrorResponse entity = new ErrorResponse();
        entity.setType(cause.getClass().getSimpleName());
        entity.setMessage(cause.getMessage());

        List<ErrorResponse.Detail> details = new ArrayList<>();
        cause.getDetails().forEach(d -> {
            details.add(new ErrorResponse.Detail(d.getField(), d.getMessage()));
        });
        entity.setDetails(details);
        return Response.status(status).entity(entity);
    }

    public static ResponseBuilder handleOtherException(Throwable cause) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        ErrorResponse entity = new ErrorResponse();
        entity.setType(cause.getClass().getSimpleName());
        entity.setMessage(cause.getMessage());
        return Response.status(status).entity(entity);
    }
}
