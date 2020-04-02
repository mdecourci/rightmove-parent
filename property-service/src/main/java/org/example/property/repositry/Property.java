package org.example.property.repositry;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.property.exceptions.NoAverageValueException;

@Builder(builderClassName = "Builder")
@EqualsAndHashCode
@ToString
public class Property {

    private long id;
    private long price;
    private Building building;
    private Address address;
    private String type;

    public Property(long id, long price, Building building, Address address, String type) {
        this.id = id;
        this.price = price;
        this.building = building;
        this.address = address;
        this.type = type;
    }

    public Property() {
    }

    public boolean validate() {
        if (address == null) {
            throw new NoAverageValueException("No address");
        }
        if (address.getPostcode() == null) {
            throw new NoAverageValueException("No postcode");
        }
        return true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
