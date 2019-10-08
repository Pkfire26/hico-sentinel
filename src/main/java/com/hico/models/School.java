package com.hico.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;
import java.util.HashSet;


@Getter
@Setter
@ToString
@Document
public class School {

    @Id
    private String id;

    @Indexed
    private String          name;
    private Address         address;
    private List<String>    phoneNos;
    private HashSet<String> schoolAdmins;

    // code for self registration to school.
    private String regCode;

    public School() {}

    public School(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
