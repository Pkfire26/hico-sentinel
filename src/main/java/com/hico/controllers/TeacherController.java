package com.hico.controllers;

import static org.springframework.http.ResponseEntity.ok;

import com.hico.models.Teacher;
import com.hico.models.TeacherInfo;
import com.hico.models.User;
import com.hico.models.School;

import com.hico.repositories.TeacherRepository;
import com.hico.repositories.SchoolRepository;
import com.hico.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hico.models.*;
import com.hico.exception.*;
import com.hico.services.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherRepository teachers;

    @Autowired
    private UserRepository users;

    @Autowired
    private SchoolRepository schools;

    @Autowired
    private CustomUserDetailsService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<TeacherInfo> getAllTeachers() {

        User sessionUser = userService.getLoggedInUser();
        List<Teacher> idList;
        boolean includeSchool = false;
        if (sessionUser.getRoles().contains(new Role(Role.ADMIN))) {
            idList = teachers.findAll();
            includeSchool = true;
        } else if  (sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) {
            checkValidSchoolAdmin(sessionUser);
            idList = teachers.findBySchoolId(sessionUser.getSchoolId());
        } else {
            throw new InsufficentAccessException("Insufficent access rights - access denied");
        }

        List<TeacherInfo> teacherList = new ArrayList<TeacherInfo>();
        for (Teacher teacher : idList) {
            TeacherInfo info = getTeacherInfo(teacher, includeSchool);
            if (info == null) {
                continue;
            }
            teacherList.add(info);
        }
        return  teacherList;
    }

    // Get a teacher details by ID. 
    // Admin can get all teachers, SchoolAdmin can get only teachers for that
    // specific school
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TeacherInfo getTeacherById(@PathVariable("id") String id) {

        User sessionUser = userService.getLoggedInUser();
        boolean includeSchool = false;
        if (sessionUser.getRoles().contains(new Role(Role.ADMIN))) {
            includeSchool = true;
        } else if  (sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) {
            checkValidSchoolAdmin(sessionUser);
        } else {
            throw new InsufficentAccessException("Insufficent access rights - access denied");
        }

        Teacher teacher = teachers.findById(id).get();
        TeacherInfo info = getTeacherInfo(teacher, includeSchool);
        if (info == null) {
            throw new RecordNotFoundException("Teacher info not found for id: " + id);
        }
        return info;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void modifyTeacherById(@PathVariable("id") String id, @Valid @RequestBody Teacher teacher) {
        teacher.setId(id);
        teachers.save(teacher);
    }

    // get a teacher's role.
    @RequestMapping(value = "/{id}/role", method = RequestMethod.GET)
    public ResponseEntity getTeachersRoles(@PathVariable("id") String id) {

        User sessionUser = userService.getLoggedInUser();
        checkValidSchoolAdmin(sessionUser);

        System.out.println("Get role for teacher with teacher Id: " + id);
        Teacher teacher = teachers.findByIdAndSchoolId(id, sessionUser.getSchoolId());
        if (teacher == null) {
            throw new RecordNotFoundException("no teacher with teacher id: " + id);
        }

        User user = users.findByIdAndSchoolId(teacher.getUserId(), teacher.getSchoolId());
        if (user == null) {
            throw new SentinelServerInternalError("teacher with  no user");
        }

        Map<Object, Object> model = new HashMap<>();
        model.put(Role.SCHOOL_ADMIN, false);
        model.put(Role.TEACHER, false);

        for (Role role: user.getRoles()) {
            if (role.equals(new Role(Role.SCHOOL_ADMIN))) {
                model.put(Role.SCHOOL_ADMIN, true);
            } else if (role.equals(new Role(Role.TEACHER))) {
                model.put(Role.TEACHER, true);
            }
        }

        return ok(model);
    }

    // update a teacher's role.
    @RequestMapping(value = "/{id}/role", method = RequestMethod.PUT)
    public void modifyTeachersRoles(@PathVariable("id") String id,
            @Valid @RequestBody Map<Object, Object> roles) {

        User sessionUser = userService.getLoggedInUser();
        checkValidSchoolAdmin(sessionUser);

        Teacher teacher = teachers.findByIdAndSchoolId(id, sessionUser.getSchoolId());
        if (teacher == null) {
            throw new RecordNotFoundException("no teacher with teacher id: " + id);
        }

        User user = users.findByIdAndSchoolId(teacher.getUserId(), teacher.getSchoolId());
        if (user == null) {
            throw new SentinelServerInternalError("teacher with  no user");
        }

        School school = schools.findById(sessionUser.getSchoolId()).get();
        if (school == null) {
            throw new SentinelServerInternalError("invalid school lookup");
        }
        HashSet<String> schoolAdmins = school.getSchoolAdmins();

        HashSet<Role> updatedRoles = new HashSet<>();
        for (Map.Entry<Object, Object> role : roles.entrySet()) {
            if ((boolean)role.getValue()) {
                System.out.println("Adding role " + role.getKey());
                updatedRoles.add(new Role((String)role.getKey()));
            }

            // update School Information
            if (((String)role.getKey()).equals(Role.SCHOOL_ADMIN)) {
                boolean modified = false;
                if ((boolean)role.getValue()) {
                    // user added as school admin
                     if (!(schoolAdmins.contains(user.getEmailId()))) {
                        schoolAdmins.add(user.getEmailId());
                        modified = true;
                     }
                } else  {
                    // user is removed from list of school admins
                    if (schoolAdmins.contains(user.getEmailId())) {
                        schoolAdmins.remove(user.getEmailId());
                        modified = true;
                    }
                }
                if (modified) {
                    school.setSchoolAdmins(schoolAdmins);
                    schools.save(school);
                }
            }
        }
        user.setRoles(updatedRoles);
        users.save(user);

        teachers.save(teacher);
    }

    private TeacherInfo getTeacherInfo(Teacher teacher, boolean includeSchool) {

        User user = users.findById(teacher.getUserId()).get();
        if (user == null) {
            System.out.println("user identity not found for teacher Id: "
                    + teacher.getId());
            return null;
        }
        School school = null;
        if (includeSchool) {
            school = schools.findById(teacher.getSchoolId()).get();
            if (school == null) {
                System.out.println("school identity not found for teacher Id: "
                        + teacher.getId());
                return null;
            }
        }
        TeacherInfo info = new TeacherInfo(teacher.getId(), user, school);
        return info;
    }

    private void checkValidSchoolAdmin(User user) {

        if (!user.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) {
            throw new InsufficentAccessException("Insufficent previlege to add teacher");
        }
        if (!user.isRegistered()) {
            throw new InsufficentAccessException(
                    "User needs to complete registration");
        }
        if (!user.isEnabled()) {
            throw new AccountDisabledException(
                    "User " + user.getEmailId() + "has been diabled");
        }
    }

    // add a new teacher to school - can only be added by School Admin
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Teacher createTeacher(@Valid @RequestBody UserRegister regInfo) {

        User sessionUser = userService.getLoggedInUser();
        checkValidSchoolAdmin(sessionUser);
        System.out.println("School Admin adding a teacher.");

        // should check for valid email 
        if (!userService.isValidEmail(regInfo.getEmailId())) {
            throw new InvalidFieldModificationException("Invalid emailId");
        }

        String schoolId = sessionUser.getSchoolId();

        School school = schools.findById(schoolId).get();
        if (school == null) {
            throw new RecordNotFoundException("No school with Id " + schoolId);
        }

        String encryptedPassword =
            new BCryptPasswordEncoder().encode(regInfo.getPassword());
        User user = new User(regInfo.getEmailId(), regInfo.getFirstName(),
                regInfo.getLastName(),
                regInfo.getCellPhoneNo(), schoolId, Role.TEACHER);

        user.setPassword(encryptedPassword);
        // save user
        users.save(user);
        // fetch the user to get Id etc
        user = users.findByEmailId(user.getEmailId());
        if (user == null) {
            throw new SentinelServerInternalError("saved user not found in Db");
        }

        Teacher teacher = new Teacher(user.getId(), schoolId);
        teachers.save(teacher);

        /*
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "Teacher added to school.");
        return ok(model);
        */

        return teacher;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteTeacher(@PathVariable String id) {
        User sessionUser = userService.getLoggedInUser();
        checkValidSchoolAdmin(sessionUser);
        System.out.println("School Admin deleting a teacher.");

        userService.deleteTeacher(id, sessionUser.getSchoolId());

    }
}
