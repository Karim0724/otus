package ru.sharipov.exception;

public class AfterInvocationException extends RuntimeException {
    public AfterInvocationException() {
    }

    public AfterInvocationException(String message) {
        super(message);
    }

    public AfterInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AfterInvocationException(Throwable cause) {
        super(cause);
    }

    public AfterInvocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
