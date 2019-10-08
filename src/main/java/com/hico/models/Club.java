package com.hico.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document
public class Club {

    @Id
    private String id;
    private String name;
    private String description;
    private String schoolId;

    //private HashSet<String> owners;
    //private HashSet<String> admins;
    //private HashSet<String> subscribers;

    public Club() {}
    public Club(String name, String schoolId) {
        this.name = name;
        this.schoolId = schoolId;
    }

}

