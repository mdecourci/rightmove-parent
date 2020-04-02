package org.rightmove.property.model;

import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "Builder")
@Data
public class Address {
    private String houseNumber;
    private String address;
    private String region;
    private String postcode;
}