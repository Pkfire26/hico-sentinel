package com.hico;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hico.models.*;
import com.hico.repositories.*;

import java.util.Arrays;
import java.util.HashSet;


@Component
public class Sentinel {
    @Autowired
    public UserRepository users;

    @Autowired
    public SchoolRepository schools;

    public static Sentinel _instance;

    public Sentinel() {}


    public static Sentinel Instance() {
        if (_instance == null) {
            synchronized (Sentinel.class) {
                _instance = new Sentinel();
            }
        }
        return _instance;
    }

    public void bootstrapAdmin() {

        // check is admin user exists.
        User admin = users.findByEmailId("admin@hico.com");
        if (admin == null) {
            System.out.println("Creating admin user for the system");
            admin = new User();
            admin.setEmailId("admin@hico.com");
            String encodedPassword = new BCryptPasswordEncoder().encode("password");
            admin.setPassword(encodedPassword);
            admin.setFirstName("Admin");
            Role adminRole = new Role("ADMIN");
            admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));
            admin.setEnabled(true);
            admin.setRegistered(true);
            users.save(admin);
        } else {
            System.out.println("Admin user exists in the system");
        }
    }
}
