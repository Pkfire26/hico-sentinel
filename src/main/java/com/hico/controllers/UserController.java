package com.hico.controllers;

import static org.springframework.http.ResponseEntity.ok;

import com.hico.models.*;
import com.hico.repositories.UserRepository;
import com.hico.exception.*;
import com.hico.services.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository users;

    @Autowired
    private CustomUserDetailsService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List getAllUsers() {
        return users.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView getUserById(@PathVariable("id") String id) {

        User user =  users.findById(id).get();
        if(user.getRoles().contains(new Role(Role.STUDENT))) {
            Student student = userService.findStudentByUserId(id);
            if (student == null) {
                 throw  new RecordNotFoundException("Invalid user id");
            }
            System.out.println("Forwarding request to student info");
            return new ModelAndView("forward:/api/student/" + student.getId());
        } else if(user.getRoles().contains(new Role(Role.TEACHER))) {
            Teacher teacher = userService.findTeacherByUserId(id);
            if (teacher == null) {
                throw  new RecordNotFoundException("Invalid user id");
            }

            System.out.println("Forwarding request to teacher info");
            return new ModelAndView("forward:/api/teacher/" + teacher.getId());
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("User", user);
        return modelAndView;
    }

    /*
    @RequestMapping(value = "/{id}/info", method = RequestMethod.GET)
    public User getUserById(@PathVariable("id") String id) {
        return users.findById(id).get();
    }
    */

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity modifyUserById(@PathVariable("id") String id,
            @Valid @RequestBody User modUser) {

        User sessionUser = userService.getLoggedInUser();
        User user = users.findById(id).get();
        if (user == null) {
            throw  new RecordNotFoundException("Invalid user id");
        }

        // only the actual user or school admin can change some bits of the user
        if ((!sessionUser.getId().equalsIgnoreCase(id)) &&
            (!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN)))) {
            throw new InsufficentAccessException("Insufficent access rights - access denied");
        }

        if (user.getEmailId() != modUser.getEmailId()) {
            throw new InvalidFieldModificationException("Cannot change users email id");
        }
        user.preCheckAccess();
        user.update(modUser);
        users.save(user);

        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User updated successfully");
        return ok(model);
    }


    @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
    public ResponseEntity modifyUserById(@PathVariable("id") String id,
        @Valid @RequestBody UserCred userCred) {

        User sessionUser = userService.getLoggedInUser();
        User user = users.findById(id).get();
        if (user == null) {
            throw  new RecordNotFoundException("Invalid user id");
        }
        user.preCheckAccess();

        // only the acutal user can change password
        if (!sessionUser.getId().equalsIgnoreCase(id)) {
            throw new InsufficentAccessException("Insufficent access rights - access denied");
        }

        String encryptedPassword =
            new BCryptPasswordEncoder().encode(userCred.getNewPassword());
        user.setPassword(encryptedPassword);
        users.save(user);

        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User password updated successfully");
        return ok(model);
    }


    // only school-admin can reset password.
    @RequestMapping(value = "/{id}/resetpassword", method = RequestMethod.PUT)
    public ResponseEntity modifyUserById(@PathVariable("id") String id) {

        User sessionUser = userService.getLoggedInUser();
        User user = users.findById(id).get();
        if (user == null) {
            throw  new RecordNotFoundException("Invalid user id");
        }
        user.preCheckAccess();
        if (!sessionUser.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) {
            throw new InsufficentAccessException("Insufficent access rights - access denied");
        }
        String resetPassword = "tempHico";
        String encryptedPassword = new BCryptPasswordEncoder().encode(resetPassword);
        user.setPassword(encryptedPassword);
        users.save(user);

        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User password reset successfully");
        return ok(model);
    }


    /*
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public User createUser(@Valid @RequestBody User user) {
        user.setId(ObjectId.get().toHexString());
        users.save(user);
        return user;
    }
    */

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String id) {
        users.delete(users.findById(id).get());
    }

}

