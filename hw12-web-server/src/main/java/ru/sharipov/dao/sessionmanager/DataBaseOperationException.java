package ru.sharipov.dao.sessionmanager;

public class DataBaseOperationException extends RuntimeException {
    public DataBaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBaseOperationException(String message) {
        super(message);
    }
}
