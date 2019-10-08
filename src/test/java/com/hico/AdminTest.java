package com.hico;

import org.testng.annotations.Test;
//import org.gradle.api.tasks.testing.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

public class AdminTest {

    @Test
    public void whenLoginRequest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("emailId", "admin@hico.com");
        jsonAsMap.put("password", "password");

        String token = 
        given().
            contentType("application/json").
            body(jsonAsMap).
        when().
            post("http://localhost:8080/api/auth/login").
        then().
            log().body().
            statusCode(200).
        extract().
            path("token");

        String id = 
        given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
        when().
            get("http://localhost:8080/api/school/").
        then().
            log().body().
            statusCode(200).
        extract().
             path("[0].id");

        given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            when().
            get("http://localhost:8080/api/school/" + id).
            then().
            log().body().
            statusCode(200);
    }


    /*
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }
    */
}

