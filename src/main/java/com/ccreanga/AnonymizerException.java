package com.ccreanga;


public class AnonymizerException extends RuntimeException {
    public AnonymizerException(String message) {
        super(message);
    }

    public AnonymizerException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnonymizerException(Throwable cause) {
        super(cause);
    }
}
