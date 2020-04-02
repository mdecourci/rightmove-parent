package org.example.property.controller;

import org.example.property.exceptions.DataNotFoundException;
import org.example.property.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PropertyServiceExceptionHandlerAdvice {

    @ResponseBody
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String propertyValidationNotFoundHandler(DataNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String propertyValidationServerExceptionHandler(ServiceException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String propertyValidationBadRequestHandler(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
