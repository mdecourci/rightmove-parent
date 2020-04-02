package org.example.property.exceptions;

public class ServiceException extends RuntimeException{
    public ServiceException() {
    }

    public ServiceException(Throwable e) {
        super(e);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
