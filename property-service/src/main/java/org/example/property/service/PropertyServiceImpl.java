package org.example.property.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.property.controller.model.PropertyType;
import org.example.property.exceptions.DataNotFoundException;
import org.example.property.exceptions.NoAverageValueException;
import org.example.property.repositry.Property;
import org.example.property.repositry.PropertyDataRepositry;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyDataRepositry propertyDataRepositry;

    @Override
    public Double calculateAveragePriceAroundPostCode(String postCode) {
        log.info("Average for postcode {}", postCode);
        var properties = propertyDataRepositry.findProperties();

        if (properties.isEmpty()) {
            throw new DataNotFoundException("Request response has no data");
        }

        if (properties.size() == 1) {
            throw new NoAverageValueException();
        }
        var validProperties = properties.stream()
                .filter(Property::validate)
                .filter((property ->
                        property.getAddress().getPostcode().substring(0,
                                property.getAddress().getPostcode().indexOf(' ')).equals(postCode)))
                .collect(Collectors.toList());

        if (validProperties.isEmpty()) {
            throw new NoAverageValueException("Invalid properties");
        }

        var average = validProperties.stream().mapToDouble(Property::getPrice).average().getAsDouble();
        log.info("Found average {} for postcode {}", average, postCode);

        return average;
    }

    @Override
    public Double calculateDifferenceInAveragePriceForTypes(PropertyType fromType, PropertyType toType) {
        log.info("Find difference of average prices for fromType = {} and toType = {}", fromType, toType);
        var properties = propertyDataRepositry.findProperties();

        if (properties.size() == 1 || fromType == toType) {
            throw new NoAverageValueException("Differences cannot be calculated with one datapoint or same types");
        }
        var typeAverages = properties.stream()
                .filter(p -> (p.getType().equals(fromType.toString()) || p.getType().equals(toType.toString())))
                .collect(groupingBy(Property::getType, averagingDouble(Property::getPrice)));

        if (typeAverages.size() < 2) {
            throw new NoAverageValueException("Differences cannot be calculated with less than 2 datapoints");
        }
        var difference = typeAverages.get(fromType.toString()) - typeAverages.get(toType.toString());

        log.info("For fromType = {} to toType = {}, difference is {}", fromType, toType, difference);
        return difference;
    }
}
