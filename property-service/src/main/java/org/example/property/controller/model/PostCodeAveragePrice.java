package org.example.property.controller.model;

import lombok.Builder;
import lombok.Value;

@Builder(builderClassName = "Builder")
@Value
public class PostCodeAveragePrice {
    private String postcode;
    private Double averagePrice;
}
