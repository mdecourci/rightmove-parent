package org.rightmove.property.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(builderClassName = "Builder")
@Data
public class Properties {
    private List<Property> properties;
}
