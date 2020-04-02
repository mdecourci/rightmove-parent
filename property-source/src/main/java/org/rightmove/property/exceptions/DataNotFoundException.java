package org.rightmove.property.exceptions;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(RuntimeException e) {
        super(e);
    }

    public DataNotFoundException() {
        super();
    }
}
