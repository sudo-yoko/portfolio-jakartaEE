package com.example.domain;

import java.util.logging.Logger;

import com.example.ApplicationDatabaseException;

import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Response.Status;

public class EJBExceptionTranslator {
    private static final Logger logger = Logger.getLogger(EJBExceptionTranslator.class.getName());
    private static final String LOG_PREFIX = ">>> [" + EJBExceptionTranslator.class.getSimpleName() + "]: ";

    public static RuntimeException translate(Throwable t, String errorDetail) {
        logger.severe(LOG_PREFIX + t.getMessage());
        if (isCausedBy(t, PersistenceException.class)) {
            return new ApplicationDatabaseException(
                    String.format("データベース例外が発生しました。[%s]", errorDetail), PersistenceException.class,
                    Status.INTERNAL_SERVER_ERROR, t);
        }
        return new RuntimeException(t);
    }

    private static boolean isCausedBy(Throwable t, Class<? extends Throwable> exceptionClass) {
        final int MAX_DEPTH = 10;

        Throwable cause = t;
        for (int i = 0; i < MAX_DEPTH; i++) {
            if (cause == null) {
                return false;
            }
            if (exceptionClass.isInstance(cause)) {
                return true;
            }
            cause = cause.getCause();
        }
        logger.warning(LOG_PREFIX + "原因例外の取得で、ループの最大回数を超えました。");
        return false;
    }
}
