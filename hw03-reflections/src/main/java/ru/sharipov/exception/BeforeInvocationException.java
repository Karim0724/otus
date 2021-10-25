package ru.sharipov.exception;

public class BeforeInvocationException extends RuntimeException {
    public BeforeInvocationException() {
    }

    public BeforeInvocationException(String message) {
        super(message);
    }

    public BeforeInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeforeInvocationException(Throwable cause) {
        super(cause);
    }

    public BeforeInvocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
