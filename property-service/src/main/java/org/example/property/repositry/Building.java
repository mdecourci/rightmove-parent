package org.example.property.repositry;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder(builderClassName = "Builder")
@EqualsAndHashCode
@ToString
public class Building {
    private int bedrooms;
    private int bathrooms;

    public Building(int bedrooms, int bathrooms) {
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
    }

    public Building() {
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }
}