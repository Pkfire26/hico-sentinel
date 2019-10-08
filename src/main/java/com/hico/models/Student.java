package com.hico.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Student {

    @Id
    public String id;

    // associated user identity
    private String userId;

    // associated school
    private String schoolId;

    //private HashSet<String> clubAssociations = new HashSet<String>();

    public Student() {}

    public Student(String userId, String schoolId) {
        this.userId = userId;
        this.schoolId = schoolId;
    }

    @Override
    public String toString() {
        return String.format(
                "Student[id='%s', userId='%s', schoolId='%s']",
                id, userId, schoolId);
    }

    /*
    public void addClubAssociation(String assocId) {
        this.clubAssociations.add(assocId);
    }

    public void removeClubAssociation(String assocId) {
        this.clubAssociations.remove(assocId);
    }
    */
}

