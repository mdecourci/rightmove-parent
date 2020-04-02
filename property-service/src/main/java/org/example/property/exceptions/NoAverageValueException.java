package org.example.property.exceptions;

public class NoAverageValueException extends RuntimeException{
    public NoAverageValueException() {
    }

    public NoAverageValueException(String msg) {
        super(msg);
    }
}
