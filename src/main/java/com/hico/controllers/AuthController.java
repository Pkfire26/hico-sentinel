package com.hico.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.security.InvalidParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.userdetails.UserDetails;


import com.hico.configs.JwtTokenProvider;
import com.hico.repositories.UserRepository;
import com.hico.repositories.SchoolRepository;
import com.hico.repositories.TeacherRepository;
import com.hico.repositories.StudentRepository;
import com.hico.services.CustomUserDetailsService;
import com.hico.models.*;
import com.hico.exception.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	UserRepository users;

    @Autowired
    SchoolRepository schools;

    @Autowired
    TeacherRepository teachers;

    @Autowired
    StudentRepository students;

	@Autowired
	private CustomUserDetailsService userService;

    @SuppressWarnings("rawtypes")
    @GetMapping("/lookup/schools")
    public List<SchoolLookup> lookupSchools(@RequestBody SchoolLookup info) {
        System.out.println("School lookup");

        String name = "";
        String zipCode = "";
        if (info.getZipCode() != null) {
            zipCode = info.getZipCode().trim();
        }
        if (info.getName() != null) {
            name = info.getName().trim();
        }

        if ((name.length() == 0) || (zipCode.length() == 0)) {
            throw new InvalidParameterException();
        }

        List<SchoolLookup> infoList;
        if (name.length() == 0) {
            infoList = schools.findByAddressZipCode(zipCode);
        } else if (zipCode.length() == 0) {
            infoList = schools.findByName(zipCode);
        } else {
            School school = schools.findByNameAndAddressZipCode(name, zipCode);
            if (school == null) {
                throw new RecordNotFoundException("No school found " + name +
                    " in zipCode  " + zipCode);
            }
            infoList = new ArrayList<SchoolLookup>();
            SchoolLookup sInfo = new SchoolLookup(name, zipCode);
            sInfo.setId(school.getId());
            infoList.add(sInfo);
        }
        return infoList;
    }

	@SuppressWarnings("rawtypes")
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody AuthBody data) {
		try {
			String username = data.getEmailId();
            System.out.println(username);
			authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            // successfully authenticated - check user status. 
            User user = users.findByEmailId(username);
            if (user == null) {
                System.out.println("authenticated user cannot be looked up by email");
                throw new SentinelServerInternalError("db error");
            }
            // prechech user access into the system
            user.preCheckAccess();

			String token = jwtTokenProvider.createToken(username,
                    this.users.findByEmailId(username).getRoles());
            System.out.printf("Username is %s:\ntoken: %s\n", username, token);
			Map<Object, Object> model = new HashMap<>();
			model.put("username", username);
			model.put("token", token);
            if (!user.isRegistered()) {
                String msg = "user " + user.getEmailId() + 
                    " has incomplete registration";
                System.out.println(msg);
                model.put("userId", user.getId());
                model.put("status", "230");
            } else {
                model.put("userId", user.getId());
                model.put("status", "200");
            }

            /*
            String role;
            if (user.getRoles().contains(new Role(Role.STUDENT))) {
                role = Role.STUDENT;
            } else if (user.getRoles().contains(new Role(Role.TEACHER))) {
                role = Role.TEACHER;
            } else if (user.getRoles().contains(new Role(Role.ADMIN))) {
                role = Role.ADMIN;
            } else if (user.getRoles().contains(new Role(Role.SCHOOL_ADMIN))) {
                role = Role.ADMIN;
            } else {
                role = Role.BASE;
            }
            */
            model.put("role", user.getRoles());

			return ok(model);
		} catch (AuthenticationException e) {
            e.printStackTrace();
			throw new BadCredentialsException("Invalid email/password supplied");
		}
	}


	@SuppressWarnings("rawtypes")
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody UserRegister info) {

		User user = userService.findUserByEmailId(info.getEmailId());
		if (user != null) {
			throw new RecordAlreadyExistsException("User with username: " + info.getEmailId() + " already exists");
		}
        if (info.getSchoolId().trim().length() == 0) {
            String msg = "Incomplete school information: need valid schoolId";
            throw new InvalidUserRegistrationException(msg);
        }
        School school = schools.findById(info.getSchoolId()).get();
        if (school == null) {
            String msg = "No school found with Id: " + info.getSchoolId();
            System.out.println(msg);
            throw new RecordNotFoundException(msg);
        }

        if (!school.getRegCode().equalsIgnoreCase(info.getRegCode())) {
            String msg = "Invalid registration code: " + info.getRegCode();
            System.out.println(msg);
            throw new InvalidUserRegistrationException(msg);
        }

        String encryptedPassword =
            new BCryptPasswordEncoder().encode(info.getPassword());
        // save user
        user = new User(info.getEmailId(), info.getFirstName(), info.getLastName(),
                info.getCellPhoneNo(), school.getId(), Role.STUDENT);
        user.setPassword(encryptedPassword);
        user.setRegistered(true);
        user.setEnabled(true);
        // save user
        users.save(user);
        // fetch the user to get Id etc
        user = users.findByEmailId(user.getEmailId());
        if (user == null) {
            throw new SentinelServerInternalError("saved user not found in Db");
        }

        Student student = new Student(user.getId(), school.getId());
        students.save(student);

		Map<Object, Object> model = new HashMap<>();
		model.put("message", "User registered successfully");
		return ok(model);
	}

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/pending/{id}", method = RequestMethod.PUT)
    public ResponseEntity completeRegistration(
            @PathVariable("id") String id, @RequestBody User modUser) {

        User user = userService.getLoggedInUser();
        System.out.println(user);
        if (!user.getEmailId().trim().equals(modUser.getEmailId().trim())) {
            String msg = "email: " + modUser.getEmailId() +
                " in registration does not belong to logged in user: " +
                user.getEmailId();
            System.out.println(msg);
            throw new InvalidUserRegistrationException(msg);
        }
        if (!user.getId().equals(id)) {
            System.out.println("Logged in Id: " + user.getId());
            System.out.println("Id: " + id);

            throw new InvalidUserRegistrationException("Invalid registration request");
        }
        School school = schools.findById(user.getSchoolId()).get();
        if (school == null) {
            throw new RecordNotFoundException("Invalid School Id");
        }
        // user with valid cred's - complete registration.
        user.completeRegistration(modUser);
        users.save(user);
        for (Role role: user.getRoles()) {
            if (role.isRole(Role.SCHOOL_ADMIN) || role.isRole(Role.TEACHER)) {
                completeTeacherRegistration(user, school);
            } else if (role.isRole(Role.STUDENT)) {
                completeStudentRegistration(user, school);
            } else {
                System.out.println("Missing role in pending user registration");
            }

        }

        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User completed pending registration successfully");
        return ok(model);
    }


    public void completeTeacherRegistration(User user, School school) {

        Teacher teacher = teachers.findByUserIdAndSchoolId(user.getId(), 
                school.getId());
        if (teacher == null) {
            teacher = new Teacher(user.getId(), school.getId());
            teachers.save(teacher);
        }
    }

    public void completeStudentRegistration(User user, School school) {

        Student student = students.findByUserIdAndSchoolId(user.getId(), 
                school.getId());

        if (student == null) {
            student = new Student(user.getId(), school.getId());
            students.save(student);
        }
    }
}
