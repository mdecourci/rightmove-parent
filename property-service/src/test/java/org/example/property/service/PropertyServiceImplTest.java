package org.example.property.service;

import org.example.property.controller.model.PropertyType;
import org.example.property.exceptions.DataNotFoundException;
import org.example.property.exceptions.NoAverageValueException;
import org.example.property.repositry.Property;
import org.example.property.repositry.PropertyDataRepositry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.example.property.PropertyTestUtils.buildProperties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertyServiceImplTest {
    @Mock
    private PropertyDataRepositry propertyDataRepositry;
    @InjectMocks
    private PropertyServiceImpl propertyService;

    @Test
    void shouldCalculateAveragePriceAroundPostCode() {
        Set<Property> propertySet = buildProperties(
                new String[]{"XY1 2AB", "XY1 2AB", "RM2 6ET"},
                new Long[]{1000l, 2000l, 3000l});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);
        var price = propertyService.calculateAveragePriceAroundPostCode("XY1");
        assertEquals(1500, price, 0.001);
    }

    @Test
    void shouldCalculateAveragePriceAroundPostCodeWithSomePrice() {
        Set<Property> propertySet = buildProperties(
                new String[]{"XY1 2AB", "XY1 2AB", "RM2 6ET"},
                new Long[]{1000l, 2000l, null});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);
        var price = propertyService.calculateAveragePriceAroundPostCode("XY1");
        assertEquals(1500, price, 0.001);
    }

    @Test
    void shouldCalculateAveragePriceAroundPostCodeWithOneProperty() {
        Set<Property> propertySet = buildProperties(
                new String[]{"XY1 2AB"},
                new Long[]{1000l});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);

        assertThrows(NoAverageValueException.class,() -> {
            propertyService.calculateAveragePriceAroundPostCode("LM9");
        });
    }

    @Test
    void shouldNotCalculateAveragePriceAroundPostCode() {
        Set<Property> propertySet = buildProperties(new String[]{"XY1 2AB", "XY1 6XY"}, new Long[]{1000l, 2000l});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);

        assertThrows(NoAverageValueException.class,() -> {
            propertyService.calculateAveragePriceAroundPostCode("LM9");

        });
    }

    @Test
    void shouldNotCalculateAverageWithNoProperties() {

        when(propertyDataRepositry.findProperties()).thenThrow(DataNotFoundException.class);

        assertThrows(DataNotFoundException.class,() -> {
            propertyService.calculateAveragePriceAroundPostCode("LM9");

        });
    }

    @Test
    void shouldNotCalculateAverageWithPropertiesNoPriceAndPostCode() {
        Set<Property> propertySet = buildProperties(new String[] {null, null, null}, new Long[]{null, null, null});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);

        assertThrows(NoAverageValueException.class,() -> {
            propertyService.calculateAveragePriceAroundPostCode("LM9");

        });
    }

    @Test
    void shouldNotCalculateAverageWithPropertiesAndNoPrice() {
        Set<Property> propertySet = buildProperties(new String[] {"XY1 2AB", "XY1 6XY"}, new Long[]{0l, 0l, 0l});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);

        assertThrows(NoAverageValueException.class,() -> {
            propertyService.calculateAveragePriceAroundPostCode("LM9");

        });
    }

    @Test
    void shouldCalculateDifferenceInAveragePriceForTypes() {
        Set<Property> propertySet = buildProperties(
                new PropertyType[]{PropertyType.FLAT, PropertyType.FLAT, PropertyType.DETACHED, PropertyType.DETACHED},
                new Long[]{1000l, 2000l, 3000l, 4000l});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);
        var price = propertyService.calculateDifferenceInAveragePriceForTypes(PropertyType.DETACHED, PropertyType.FLAT);
        assertEquals(2000, price, 0.001);
    }

    @Test
    void shouldCalculateDifferenceInAveragePriceForTypeParametersFlipped() {
        Set<Property> propertySet = buildProperties(
                new PropertyType[]{PropertyType.FLAT, PropertyType.FLAT, PropertyType.DETACHED, PropertyType.DETACHED},
                new Long[]{1000l, 2000l, 3000l, 4000l});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);
        var price = propertyService.calculateDifferenceInAveragePriceForTypes(PropertyType.FLAT, PropertyType.DETACHED);
        assertEquals(-2000, price, 0.001);
    }

    @Test
    void shouldCalculateNoDifferenceForSameAveragePrices() {
        Set<Property> propertySet = buildProperties(
                new PropertyType[]{PropertyType.FLAT, PropertyType.FLAT, PropertyType.FLAT, PropertyType.DETACHED, PropertyType.DETACHED},
                new Long[]{2500l, 6000l, 2000l, 3000l, 4000l}); // 3500

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);
        var price = propertyService.calculateDifferenceInAveragePriceForTypes(PropertyType.DETACHED, PropertyType.FLAT);
        assertEquals(0, price, 0.001);
    }

    @Test
    void shouldCalculateDifferenceInAveragePriceForOddNumberOfTypes() {
        Set<Property> propertySet = buildProperties(
                new PropertyType[]{PropertyType.FLAT, PropertyType.DETACHED, PropertyType.DETACHED},
                new Long[]{2000l, 3000l, 4000l});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);
        var price = propertyService.calculateDifferenceInAveragePriceForTypes(PropertyType.DETACHED, PropertyType.FLAT);
        assertEquals(1500, price, 0.001);
    }

    @Test
    void shouldNotCalculateDifferenceInAveragePriceForSameRequestedType() {
        Set<Property> propertySet = buildProperties(
                new PropertyType[]{PropertyType.FLAT, PropertyType.FLAT, PropertyType.DETACHED, PropertyType.DETACHED},
                new Long[]{1000l, 2000l, 3000l, 4000l});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);

        assertThrows(NoAverageValueException.class,() -> {
            propertyService.calculateDifferenceInAveragePriceForTypes(PropertyType.FLAT, PropertyType.FLAT);
        });
    }

    @Test
    void shouldNotCalculateDifferenceInAveragePriceForSameType() {
        Set<Property> propertySet = buildProperties(
                new PropertyType[]{PropertyType.FLAT, PropertyType.FLAT, PropertyType.FLAT, PropertyType.FLAT},
                new Long[]{1000l, 2000l, 3000l, 4000l});

        when(propertyDataRepositry.findProperties()).thenReturn(propertySet);

        assertThrows(NoAverageValueException.class,() -> {
            propertyService.calculateDifferenceInAveragePriceForTypes(PropertyType.DETACHED, PropertyType.FLAT);
        });
    }
}