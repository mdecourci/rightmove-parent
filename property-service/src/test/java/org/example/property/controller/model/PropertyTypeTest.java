package org.example.property.controller.model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.example.property.controller.model.PropertyType.FLAT;
import static org.junit.jupiter.api.Assertions.*;

class PropertyTypeTest {

    @Test
    void shouldFindEnum() {
        assertEquals(Optional.of(FLAT), PropertyType.find(FLAT.toString()));
    }

    @Test
    void cannotFindEnumFromInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            PropertyType.find("WE").isEmpty();
        });
    }
}