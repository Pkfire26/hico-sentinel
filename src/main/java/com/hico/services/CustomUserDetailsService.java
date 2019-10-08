package com.hico.services;

import com.hico.models.*;
//import com.hico.repositories.RoleRepository;
import com.hico.repositories.*;
import com.hico.exception.*;
import com.hico.services.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

import com.hico.exception.*;
import com.hico.repositories.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository users;

    @Autowired
    private StudentRepository students;

    @Autowired
    private TeacherRepository teachers;

    @Autowired
    private ClubAssociationRepository clubAssociations;

    /*
    @Autowired
    private RoleRepository roleRepository;
    */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CustomUserDetailsService userService;

    public User findUserByEmailId(String emailId) {
        return users.findByEmailId(emailId);
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
            "[a-zA-Z0-9_+&*-]+)*@" + 
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
            "A-Z]{2,7}$"; 

        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    } 

    public User getLoggedInUser() {
        Object principal =
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String name;
        if (principal instanceof UserDetails) {
            name = ((UserDetails)principal).getUsername();
        } else {
            name = principal.toString();
        }

        System.out.println("logged in user: " +  name);
        User user = users.findByEmailId(name);
        if (user == null) {
            String msg = "Invalid token with email " + name +
                ": - user in the token does not exist";
            System.out.println(msg);
            throw new InvalidUserTokenException(msg);
        }
        return user;
        //  throw new InvalidUserTokenException("invalid token");
    }


    public Student findStudentByUserId(String id) {
        return students.findByUserId(id);
    }

    public Teacher findTeacherByUserId(String id) {
        return teachers.findByUserId(id);
    }

    public void deleteStudent(String id) {

        Student student = students.findById(id).get();
        if (student == null) {
            throw new RecordNotFoundException("No such student with id " + id);
        }

        // delete the user and related associations
        deleteUser(student.getUserId());
        students.delete(student);
        System.out.println("Deleted student account for id: " + id);
    }

    public void deleteUser(String  id) {

        User user = users.findById(id).get();
        if (user == null) {
            throw new RecordNotFoundException("No such user with id " + id);
        }

        // delete all the user associations.
        HashSet<ClubAssociation> assocList = clubAssociations.findAllByUserId(id);
        for (ClubAssociation assoc : assocList) {
            System.out.println("Deleting aasociation with user : " + id);
        }
        users.delete(user);
    }


    public void deleteTeacher(String id, String schoolId) {
        Teacher teacher = teachers.findByIdAndSchoolId(id, schoolId);
        if (teacher == null) {
            throw new RecordNotFoundException("No such teacher with Id: " + id);
        }
        deleteUser(teacher.getUserId());
        teachers.delete(teacher);
    }

    public void saveUser(User user) {
        System.out.println("save user");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        /*
        Role userRole = roleRepository.findByRole("ADMIN");
        Role userRole = new Role("ADMIN");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        */
        users.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {

        System.out.println("load user by username");
        User user = users.findByEmailId(emailId);
        System.out.println(user);
        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        System.out.println("getUserAuthority");
        System.out.println(userRoles);
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        System.out.println(grantedAuthorities);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        System.out.println("buildUserForAuthentication");
        System.out.println("user: " + user.getEmailId());
        System.out.println("password: " + user.getPassword());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmailId(), user.getPassword(), authorities);
        System.out.println(userDetails);
        System.out.println(userDetails.getPassword());
        return userDetails;
    }
}
