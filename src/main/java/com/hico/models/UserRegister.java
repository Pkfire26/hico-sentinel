package com.hico.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRegister {

    private String emailId;
    private String firstName;
    private String lastName;
    private String password;
    private String cellPhoneNo;
    private String schoolId;
    private String regCode;

    // Teacher / Student
    private Role role;

    // required for student registration
    private String identCode;

    public UserRegister() {}

    public UserRegister(String emailId, String firstName, String lastName,
            String password, String cellPhoneNo, String schoolId, String regCode) {
        this.emailId = emailId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhoneNo = cellPhoneNo;
        this.schoolId = schoolId;
        this.regCode = regCode;
    }
}
