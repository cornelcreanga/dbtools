package com.ccreanga;

public class IOExceptionRuntime extends RuntimeException {

    public IOExceptionRuntime(Throwable cause) {
        super(cause);
    }

    public IOExceptionRuntime(String message) {
        super(message);
    }
}
