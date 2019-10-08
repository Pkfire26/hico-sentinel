
package com.hico.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Document
public class Teacher {

    @Id
    public String id;

    // User identity for this teacher
    private String userId;

    // school association
    private String schoolId;

    public Teacher() {}

    public Teacher(String userId, String schoolId) {
        this.userId = userId;
        this.schoolId = schoolId;
    }

    // id's of club associations.
    private HashSet<String> clubAssociations = new HashSet<String>();

    @Override
    public String toString() {
        return String.format(
                "Teacher[id=%s, userId='%s', schoolId='%s']",
                id, userId, schoolId);
    }

    public void addClubAssociation(String assocId) {
        this.clubAssociations.add(assocId);
    }

    public void removeClubAssociation(String assocId) {
        this.clubAssociations.remove(assocId);
    }
}
