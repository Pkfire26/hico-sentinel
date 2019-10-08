package com.hico.models;

import com.hico.exception.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashSet;
import com.hico.models.Teacher;
import com.hico.models.Student;

@Document
public class User {

    @Id
    public String id;

    // User Profile Information
    @Indexed(unique = true)
    private String emailId;
    private String firstName;
    private String lastName;
    private String password;
    private String cellPhoneNo;

    // user is enabled / disabled.
    private boolean enabled;

    // user registration is complete.
    private boolean registered;

    // roles associated with the user.
    private HashSet<Role>   roles = new HashSet<Role>();

    // School Id;
    private String schoolId;

    // code for registration;
    private String regCode;

    public User() {}

   public User(String emailId, String firstName, String lastName,
           String cellPhoneNo, String schoolId, String role) {
       this.emailId = emailId;
       this.firstName = firstName;
       this.lastName = lastName;
       this.cellPhoneNo = cellPhoneNo;
       this.schoolId = schoolId;
       this.roles.add(new Role(role));

   }

   public void setId(String id) {
       this.id = id;
   }

   public String getId() {
       return this.id;
   }

   public void setPassword(String password) {
       this.password = password;
   }

   public String getPassword() {
       return this.password;
   }

   public void setEnabled(boolean enabled) {
        this.enabled = enabled;
   }

   public boolean isEnabled() {
       return enabled;
   }

   public void setRegistered(boolean registered) {
       this.registered = registered;
   }

   public boolean isRegistered() {
       return registered;
   }

   public String getFirstName() {
       return this.firstName;
   }

   public void setFirstName(String firstName) {
       this.firstName = firstName;
   }

   public String getLastName() {
       return this.lastName;
   }

   public String getFullName() {
       return this.firstName + " " + this.lastName;
   }

   public void setEmailId(String emailId) {
       this.emailId = emailId;
   }

   public String getEmailId() {
        return this.emailId;
   }

   public HashSet<Role> getRoles() {
       return this.roles;
   }

   public void setRoles(HashSet<Role> roles) {
       this.roles = roles;
   }

   public void setCellPhoneNo(String cellPhoneNo) {
       this.cellPhoneNo = cellPhoneNo;
   }

   public String getCellPhoneNo() {
       return this.cellPhoneNo;
   }

   public void setSchoolId(String schoolId) {
       this.schoolId = schoolId;
   }
   public String getSchoolId() {
       return this.schoolId;
   }

   @Override
   public String toString() {
       return "email: " + getEmailId() + "\n password: " + getPassword() 
            + "\nRole: " + getRoles() + "\n";

   }

   public void preCheckAccess() {
       if (this.isRegistered() && !this.isEnabled()) {
           String msg = "Account of user " + this.getEmailId() 
               + " has been disabled";
           System.out.println(msg);
           throw new AccountDisabledException(msg);
       }
   }

   public void completeRegistration(User modUser) {
       update(modUser);
       setEnabled(true);
       setRegistered(true);
   }

   public void update(User modUser) {

       // check registration code.
       if (modUser.getFirstName().length() > 0 ) {
           this.firstName = modUser.getFirstName();
       }
       if (modUser.getLastName().length() > 0 ) {
           this.lastName = modUser.getLastName();
       }
       if (modUser.getCellPhoneNo().length() > 0 ) {
           // validate cell phone number
           this.cellPhoneNo = modUser.getCellPhoneNo();
       }
   }

}

