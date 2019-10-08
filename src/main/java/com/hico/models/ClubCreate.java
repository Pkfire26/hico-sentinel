package com.hico.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.HashSet;

@Getter
@Setter
@ToString
public class ClubCreate {

    private String id;
    private String name;
    private String description;

    private HashSet<String> supervisors;
    // email id's of the students.
    private HashSet<String> clubAdmins;

    public ClubCreate() {}
    public ClubCreate(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

