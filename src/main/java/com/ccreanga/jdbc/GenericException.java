package com.ccreanga.jdbc;

public class GenericException extends RuntimeException {

    public GenericException(String message) {
        super(message);
    }

    public GenericException(Throwable cause) {
        super(cause);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }
}
