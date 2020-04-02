package org.example.property.repositry;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Builder(builderClassName = "Builder")
@EqualsAndHashCode
@ToString
public class Properties {
    public Properties() {
    }

    public Properties(List<Property> properties) {
        this.properties = properties;
    }

    private List<Property> properties;

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
