package org.rightmove.property.model;

import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "Builder")
@Data
public class Property {
    private long id;
    private long price;
    private Building building;
    private Address address;
    private String type;
}
