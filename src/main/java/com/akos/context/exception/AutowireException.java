package com.akos.context.exception;

public class AutowireException extends RuntimeException {

    public AutowireException(String message) {
        super(message);
    }

    public AutowireException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutowireException(Throwable cause) {
        super(cause);
    }
}
