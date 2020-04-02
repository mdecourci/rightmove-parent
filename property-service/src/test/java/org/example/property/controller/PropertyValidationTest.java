package org.example.property.controller;

import org.example.property.controller.model.PropertyType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyValidationTest {

    private PropertyValidation propertyValidation = new PropertyValidation();

    @Test
    void validateCorrectPostCode() {
        assertDoesNotThrow(() -> {
            propertyValidation.validatePostCode("GU13");

        });
    }

    @Test
    void inValidateInCorrectPostCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            propertyValidation.validatePostCode("111 A");

        });
    }

    @Test
    void inValidateBlankPostCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            propertyValidation.validatePostCode("");

        });
    }

    @Test
    void inValidateNullPostCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            propertyValidation.validatePostCode(null);

        });
    }

    @Test
    void inValidateNumericPostCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            propertyValidation.validatePostCode("11");

        });
    }

    @Test
    void validateCorrectType() {
        assertDoesNotThrow(() -> {
            propertyValidation.validateType(PropertyType.DETACHED.toString());

        });
    }

    @Test
    void inValidateInCorrectType() {
        assertThrows(IllegalArgumentException.class, () -> {
            propertyValidation.validateType("Harry");

        });
    }
}