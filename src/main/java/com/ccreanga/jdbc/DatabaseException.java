package com.ccreanga.jdbc;

public class DatabaseException extends RuntimeException {
    public DatabaseException(Throwable cause) {
        super(cause);
    }

    public DatabaseException(String message) {
        super(message);
    }
}
