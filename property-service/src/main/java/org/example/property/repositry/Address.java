package org.example.property.repositry;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder(builderClassName = "Builder")
@EqualsAndHashCode
@ToString
public class Address {
    private String houseNumber;
    private String address;
    private String region;
    private String postcode;

    public Address(String houseNumber, String address, String region, String postcode) {
        this.houseNumber = houseNumber;
        this.address = address;
        this.region = region;
        this.postcode = postcode;
    }

    public Address() {
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}