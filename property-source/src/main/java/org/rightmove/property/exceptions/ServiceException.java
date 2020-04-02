package org.rightmove.property.exceptions;

public class ServiceException extends RuntimeException {
    public ServiceException(RuntimeException e) {
        super(e);
    }

    public ServiceException() {
        super();
    }
}
