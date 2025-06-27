package com.example;

import jakarta.ws.rs.core.Response;

public class ApplicationDatabaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Class<? extends Throwable> type;
    private final int status;

    public ApplicationDatabaseException(String message, Class<? extends Throwable> type, Response.Status status) {
        super(message);
        this.type = type;
        this.status = status.getStatusCode();
    }

    public ApplicationDatabaseException(String message, Class<? extends Throwable> type, Response.Status status,
            Throwable cause) {
        super(message, cause);
        this.type = type;
        this.status = status.getStatusCode();
    }

    public Class<? extends Throwable> getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }
}
