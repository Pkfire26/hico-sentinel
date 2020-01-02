package com.hico.controllers;

import com.hico.models.*;
import com.hico.exception.*;

import com.hico.repositories.*;

import com.hico.services.CustomUserDetailsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentRepository students;

    @Autowired
    private UserRepository users;

    @Autowired
    private SchoolRepository schools;

    @Autowired
    private ClubAssociationRepository clubAssociations;

    @Autowired
    private CustomUserDetailsService userService;

    // get all students 
    // admin can see all students
    // school admin / teacher can see all students in the school
    // 
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<StudentInfo> getAllStudents() {

        User sessionUser = userService.getLoggedInUser();

        List<Student> studentList = null;
        if (sessionUser.getRoles().contains(new Role(Role.ADMIN))) {
            studentList = students.findAll();
        } else if ((sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) ||
            (sessionUser.getRoles().contains(new Role(Role.TEACHER)))) {
            studentList =  students.findAllBySchoolId(sessionUser.getSchoolId());
        }

        if (studentList != null) {
            List<StudentInfo> infoList = getStudentInfoListFromStudentList(studentList);
            return infoList;
        }
        throw new InsufficentAccessException(
                "Insufficent access -  cannot access student info");
    }


    // get user details
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public StudentInfo getStudentById(@PathVariable("id") String id) {

        User sessionUser = userService.getLoggedInUser();
        Student sessionStudent = userService.findStudentByUserId(sessionUser.getId());

        if ((sessionStudent != null &&
                 !sessionUser.getId().equalsIgnoreCase(sessionStudent.getUserId())) &&
                (!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN)))) {
            throw new InsufficentAccessException(
                    "Insufficent access -  cannot access student info");

        }

        Student student = students.findByUserId(id);
        if (student == null) {
            throw new RecordNotFoundException("No such student with id " + id);
        }
        StudentInfo info = getStudentInfoForStudent(student);
        return info;

    }

    private StudentInfo getStudentInfoForStudent(Student student) {
        User user = users.findById(student.getUserId()).get();
        if (user == null) {
            throw new SentinelServerInternalError(
                    "user info missing for student " + student.getId());
        }
        System.out.println(user);
        School school = schools.findById(student.getSchoolId()).get();
        if (school == null) {
            throw new SentinelServerInternalError(
                    "school info missing for student " + student.getId());
        }

        HashSet<ClubAssociation> assocList = clubAssociations.findAllByUserId(user.getId());

        user.setPassword("");
        StudentInfo info = new StudentInfo(user, school, assocList);
        info.setClubAssociations(assocList);
        return info;
    }

    private List<StudentInfo> getStudentInfoListFromStudentList(List<Student> studentList) {

        ArrayList<StudentInfo> infoList = new ArrayList<StudentInfo>();
        for (Student student : studentList) {
            StudentInfo info = getStudentInfoForStudent(student);
            infoList.add(info);
        }
        return infoList;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void modifyStudentById(@PathVariable("id") String id, @Valid @RequestBody Student student) {
        student.setId(id);
        students.save(student);
    }

    /*
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Student createStudent(@Valid @RequestBody Student student) {
        student.setId(ObjectId.get().toHexString());
        students.save(student);
        return student;
    }
    */

    // only student self and school admin can delete the student account.
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteStudent(@PathVariable String id) {

        User sessionUser = userService.getLoggedInUser();
        if ((!sessionUser.getId().equalsIgnoreCase(id)) &&
                (!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN)))) {
            throw new InsufficentAccessException(
                    "Insufficent access -  cannot delete student");
        }
        userService.deleteStudent(id);
    }
}
