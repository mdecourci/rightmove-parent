package org.example.property.service;

import org.example.property.controller.model.PropertyType;

public interface PropertyService {
    Double calculateAveragePriceAroundPostCode(String postCode);

    Double calculateDifferenceInAveragePriceForTypes(PropertyType fromType, PropertyType toType);
}
