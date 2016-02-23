package com.ccreanga;


public class AnonymizerException extends RuntimeException {
    public AnonymizerException(String message) {
        super(message);
    }

    public AnonymizerException(Throwable cause) {
        super(cause);
    }
}
