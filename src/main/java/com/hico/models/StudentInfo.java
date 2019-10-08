package com.hico.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.hico.models.*;
import java.util.List;

@Getter
@Setter
@ToString
public class StudentInfo {

    private User    user;
    private School  school;
    private HashSet<ClubAssociation> clubAssociations = new HashSet<ClubAssociation>();

    public StudentInfo() {}

    public StudentInfo(User user, School school, HashSet<ClubAssociation> clubs) {
        this.user = user;
        this.school = school;
        this.clubAssociations = clubAssociations;
        System.out.println("no of clubs = " + this.clubAssociations.size());
    }
}
