package com.hico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;



@SpringBootApplication
@Component
public class Application  extends SpringBootServletInitializer {

    @Autowired
    private Sentinel sentinel;

   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(Application.class);
   }

   public static void main(String[] args) {
       System.out.println("Starting Sentinel...");
       SpringApplication application = new SpringApplication(Application.class);
       application.run(args);
   }

    @Bean
    CommandLineRunner init() {
    //CommandLineRunner init(RoleRepository roleRepository) {

        return args -> {
        };
    }

   @PostConstruct
   public void bootstrap() {
        sentinel.bootstrapAdmin();
   }

   @RequestMapping(value = "/")
   public String hello() {
      return "Hello World from HiCo";
   }
}

