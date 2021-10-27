package com.sidorovich.pavel.buber.exception;

public class CouldNotInitializeConnectionPoolError extends Error {
    public CouldNotInitializeConnectionPoolError(String message) {
        super(message);
    }

    public CouldNotInitializeConnectionPoolError(String message, Throwable cause) {
        super(message, cause);
    }
}
