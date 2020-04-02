package org.rightmove.property.model;

import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "Builder")
@Data
public class Building {
    private int bedrooms;
    private int bathrooms;
}