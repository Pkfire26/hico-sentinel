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
public class ClubInfo {

    private String id;
    private String name;
    private String description;
    private String schoolId;

    public ClubInfo() {}
    public ClubInfo(String name, String description,String schoolId) {
        this.name = name;
        this.description = description;
        this.schoolId = schoolId;
    }
}

