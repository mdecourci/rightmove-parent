package org.example.property.exceptions;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException() {
    }

    public DataNotFoundException(Throwable e) {
        super(e);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotFoundException(String msg) {
        super(msg);
    }
}
