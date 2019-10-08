package com.hico.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

public class Role {

    public static final String ADMIN        = "ADMIN";
    public static final String SCHOOL_ADMIN = "SCHOOL-ADMIN";
    public static final String TEACHER      = "TEACHER";
    public static final String STUDENT      = "STUDENT";

    // a user with no role yet - just a account in the system
    public static final String BASE      = "BASE";

    private String role;
    public Role() {}

    public Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public boolean isRole(String role) {
        return this.role.equalsIgnoreCase(role);
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (role == null) {
            return false;
        }

        Role role = (Role)obj;
        return this.role.equalsIgnoreCase(role.getRole());
    }
}
