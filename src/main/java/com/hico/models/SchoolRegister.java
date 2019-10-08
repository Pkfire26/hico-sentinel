package com.hico.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.HashSet;

@Getter
@Setter
@ToString
public class SchoolRegister {

    private String          name;
    private Address         address;
    private List<String>    phoneNos;
    private HashSet<String> schoolAdmins;
    private String          regCode;

    public SchoolRegister() {}

    public SchoolRegister(String name, Address address,
            List<String> phoneNos, HashSet<String> schoolAdmins,
            String regCode) {
        this.name = name;
        this.address = address;
        this.phoneNos = phoneNos;
        this.schoolAdmins = schoolAdmins;
        this.regCode = regCode;
    }
}
