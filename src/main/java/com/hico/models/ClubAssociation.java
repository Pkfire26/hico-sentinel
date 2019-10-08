package com.hico.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.HashSet;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document
public class ClubAssociation {

    // Club roles - teachers
    public static final String CLUB_SUPERVISOR      = "CLUB-SUPERVISOR";
    public static final String CLUB_ADVISOR         = "CLUB-ADVISOR";

    // Club roles - students
    public static final String CLUB_ADMIN           = "CLUB-ADMIN";
    public static final String CLUB_MEMBER          = "CLUB-MEMBER";
    public static final String CLUB_SUBSCRIBER      = "CLUB-SUBSCRIBER";

    public static final String StatusPending        = "Pending";
    public static final String StatusApproved       = "Approved";
    public static final String StatusSuspended      = "Suspended";

    @Id
    private String   id;

    private String   clubId;
    private String   userId;
    private HashSet<String>  clubRoles = new HashSet<String>();
    private Date     joinDate;
    private String   status;

    public ClubAssociation() {}

    public ClubAssociation(String clubId, String userId) {
        this.clubId = clubId;
        this.userId = userId;
    }

    public void addRole(String role) {
        this.clubRoles.add(role);
    }

    public void removeRole(String role) {
        this.clubRoles.remove(role);
    }
}

