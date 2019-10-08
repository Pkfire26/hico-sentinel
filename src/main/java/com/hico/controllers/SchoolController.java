package com.hico.controllers;

import com.hico.models.*;
import com.hico.exception.*;
import com.hico.services.CustomUserDetailsService;

import com.hico.repositories.SchoolRepository;
import com.hico.repositories.UserRepository;
import com.hico.repositories.ClubRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
@RequestMapping("/api/school")
public class SchoolController {

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private UserRepository users;

    @Autowired
    private SchoolRepository schools;

    @Autowired
    private ClubRepository clubs;

    // Get all schools - only valid 
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<School> getAllSchools() {

        User sessionUser = userService.getLoggedInUser();
        if (!sessionUser.getRoles().contains(new Role(Role.ADMIN))) {
            // this needs to redirect to method below with 'id'
           throw new InsufficentAccessException("Insufficent access rights - access denied");
        }
        return schools.findAll();
    }

    // Get school details by ID.
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public School getSchoolById(@PathVariable("id") String id) {

        User sessionUser = userService.getLoggedInUser();
        // only admin or folks associated with the school can access this
        if (!(sessionUser.getRoles().contains(new Role(Role.ADMIN))) &&
           (!sessionUser.getSchoolId().equalsIgnoreCase(id))) {
            throw new InsufficentAccessException("Insufficent access rights - access denied");
        }
        return schools.findById(id).get();
    }

    // Get list of clubs in my (session user)  school
    @RequestMapping(value = "/{id}/clubs", method = RequestMethod.GET)
    public HashSet<Club> getMyClubs(@PathVariable("id") String id) {

        User sessionUser = userService.getLoggedInUser();
        // only admin or folks associated with the school can access this
        if (!(sessionUser.getRoles().contains(new Role(Role.ADMIN))) &&
                (!sessionUser.getSchoolId().equalsIgnoreCase(id))) {
            throw new InsufficentAccessException("Insufficent access rights - access denied");
        }

        HashSet<Club> clubList = clubs.findAllBySchoolId(sessionUser.getSchoolId());
        return clubList;
    }

    // Update School details
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void modifySchoolById(@PathVariable("id") String id,
            @Valid @RequestBody SchoolRegister info) {

        /*
        school.setId(id);
        schools.save(school);
        */
        System.out.println("Not Implemented");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public School createSchool(@Valid @RequestBody SchoolRegister info) {
        System.out.println("createSchool: " + info);
        School school = schools.findByNameAndAddressZipCode(info.getName(),
                info.getAddress().getZipCode());
        if (school != null) {
            String  msg = "School: " + school.getName() +
                " already exists in zipcode: " + school.getAddress().getZipCode();
            System.out.println(msg);
            throw new  RecordAlreadyExistsException(msg);
        }
        school = new School(info.getName(), info.getAddress());
        school.setId(ObjectId.get().toHexString());
        school.setPhoneNos(info.getPhoneNos());
        school.setRegCode(info.getRegCode());
        school = schools.save(school);
        String schoolId = school.getId();
        HashSet<String> schoolAdmins = new HashSet<String>();
        info.getSchoolAdmins().forEach((emailId) -> {
            System.out.println(emailId);
            User adminUser = users.findByEmailId(emailId);
            if (adminUser == null) {
                System.out.println("user with email  " + emailId + 
                        " does not exist - adding");
                User user = new User(emailId, "", "", "", schoolId,
                        Role.SCHOOL_ADMIN);
                // fix this to be random and email should contain the temporary password
                String encodedPassword = new BCryptPasswordEncoder().encode("hico123");
                user.setPassword(encodedPassword);
                schoolAdmins.add(emailId);
                // send email to user
                // mark the user not yet registered.
                users.save(user);
            } else {
                System.out.println("user with email " + emailId + " already exists");
                schoolAdmins.add(emailId);
                // send email to user
            }
        });

        school.setSchoolAdmins(schoolAdmins);
        school = schools.save(school);

        return school;
    }

    // os
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteSchool(@PathVariable String id) {


        schools.delete(schools.findById(id).get());
    }

}
