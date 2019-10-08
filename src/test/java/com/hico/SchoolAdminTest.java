package com.hico;

import org.testng.annotations.Test;
//import org.gradle.api.tasks.testing.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;


import java.util.HashMap;
import java.util.Map;

public class SchoolAdminTest {

    @Test
    public void whenLoginRequest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("emailId", "foo-1@bar.com");
        jsonAsMap.put("password", "hico123");

        Response resp =
        given().
            contentType("application/json").
            body(jsonAsMap).
        when().
            post("http://localhost:8080/api/auth/login").
        then().
            log().body().
            statusCode(anyOf(is(200), is(230))).
        extract().
            response();


        String code = resp.jsonPath().get("status");
        String userId = resp.jsonPath().get("userId");
        String token = resp.jsonPath().get("token");
        System.out.println(userId);
        System.out.println(token);
        if (code.equals("200")) {

        } else if (code.equals("230")) {

        }


        /*
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
            */
    }


    /*
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }
    */
}

