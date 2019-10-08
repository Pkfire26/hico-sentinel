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
public class ClubMember {

    private User    user;
    private HashSet<String>  clubRoles = new HashSet<String>();
    private String status;

    public ClubMember() {}

    public ClubMember(User user, HashSet<String> roles) {
        this.user = user;
        this.clubRoles = roles;
    }
}
