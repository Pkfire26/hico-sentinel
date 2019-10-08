
package com.hico.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SchoolLookup {

    private String id;
    private String name;
    private String zipCode;

    public SchoolLookup() {}

    public SchoolLookup(String name, String zipCode) {
        this.name = name;
        this.zipCode = zipCode;
    }
}
