
package com.hico.models;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class TeacherInfo {

    public String id;

    // User identity for this teacher
    private User user;

    // school association
    private School school;

    public TeacherInfo() {}

    public TeacherInfo(String id, User user, School school) {
        this.id = id;
        this.user = user;
        this.school = school;
    }

    @Override
    public String toString() {
        if (school != null) {
        return String.format(
                "Teacher[id=%s,\n user='%s'\n, school='%s']", id, user, school);
        } else {
            return String.format("Teacher[id=%s,\n user='%s'\n]", id, user);
        }

    }
}
