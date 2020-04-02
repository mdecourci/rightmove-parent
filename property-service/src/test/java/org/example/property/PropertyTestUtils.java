package org.example.property;

import org.example.property.controller.model.PropertyType;
import org.example.property.repositry.Address;
import org.example.property.repositry.Property;

import java.util.HashSet;
import java.util.Set;

public class PropertyTestUtils {

    public static Set<Property> buildProperties(String[] postCodes, Long[] prices) {
        var propertySet = new HashSet<Property>();

        if (postCodes != null && prices != null) {
            for (int i = 0; i < postCodes.length; i++) {
                var propertyBuilder = Property.builder();
                if (prices[i] != null) {
                    propertyBuilder = propertyBuilder.price(prices[i]);
                }
                if (postCodes[i] != null) {
                    propertyBuilder = propertyBuilder.address(Address.builder().postcode(postCodes[i]).build());
                }
                propertySet.add(propertyBuilder.build());
            }
        }

        return propertySet;
    }

    public static Set<Property> buildProperties(PropertyType[] propertyTypes, Long[] prices) {
        var propertySet = new HashSet<Property>();

        if (propertyTypes != null && prices != null) {
            for (int i = 0; i < propertyTypes.length; i++) {
                var propertyBuilder = Property.builder();
                if (prices[i] != null) {
                    propertyBuilder = propertyBuilder.price(prices[i]);
                }
                if (propertyTypes[i] != null) {
                    propertyBuilder = propertyBuilder.type(propertyTypes[i].toString());
                }
                propertySet.add(propertyBuilder.build());
            }
        }

        return propertySet;
    }
}
