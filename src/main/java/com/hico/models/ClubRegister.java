package com.hico.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClubRegister {

    private String clubId;
    private boolean isMember;

    public ClubRegister() {}
    public ClubRegister(String clubId, boolean isMember ) {
        this.clubId = clubId;
        this.isMember = isMember;
    }

    public boolean getIsMember() {
        return this.isMember;
    }
}

