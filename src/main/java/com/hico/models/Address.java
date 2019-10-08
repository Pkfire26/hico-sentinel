
package com.hico.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class Address {

    public String street;
    public String city;
    public String state;
    public String zipCode;
    public String country;

    public Address(String street, String city,
            String state, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
}
