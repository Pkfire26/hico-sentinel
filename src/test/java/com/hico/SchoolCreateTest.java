package com.hico;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
//import org.gradle.api.tasks.testing.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class SchoolCreateTest {


    @DataProvider(name="createSchoolJson")
    public Object[][] createSchoolData() {
        return new Object[][] {
         { TestData.school_info_1 },
         { TestData.school_info_2 }
       };
    }

    @Test(dataProvider="createSchoolJson", alwaysRun=true)
    public void schoolCreateTest(String payload) {
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
            body(payload).
            header("Authorization", "Bearer " + token).
        when().
            post("http://localhost:8080/api/school/").
        then().
            log().body().
            statusCode(200).
        extract().
             path("id");

        System.out.println("ID: " + id);
        given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            when().
            get("http://localhost:8080/api/school/" + id).
            then().
            log().body().
            statusCode(200);
    }

    @DataProvider(name="schoolAdminFirstLoginData")
    public Object[][] schoolAdminFirstLoginData() {
        try {
        return new Object[][] {
            { TestData.school_1_admin_login_1, TestData.register_payload_1},
            { TestData.school_1_admin_login_2, TestData.register_payload_2},
            { TestData.school_2_admin_login_1, TestData.register_payload_3},
            { TestData.school_2_admin_login_2, TestData.register_payload_4}
        };
        } catch (Throwable e) {
            e.printStackTrace();
            return new Object[][] {{}};
        }
    }

    /*
    @DataProvider(name="adminRegisterData")
    public Object[][] adminRegisterData() {
        return new Object[][] {
            { register_payload_1 },
            { register_payload_2 },
            { register_payload_3 },
            { register_payload_4 }
        };
    }
    */


    @Test(dataProvider="schoolAdminFirstLoginData", alwaysRun=true)
    public void schoolAdminRegisterTest(String loginPayload, String registerPayload) {

        Response resp = 
            given().
               contentType("application/json").
               body(loginPayload).
               log().body().
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

        if (code.equals("200")) {
            System.out.println("User is already registered");
            return;
        } else if (code.equals("230")) {
            System.out.println("User needs to be registered");
        }


        Response response =
            given().
               contentType("application/json").
               header("Authorization", "Bearer " + token).
               body(registerPayload).
            when().
               put("http://localhost:8080/api/auth/pending/" + userId).
            then().
               log().body().
               statusCode(200).
            extract().
                response();

         System.out.println("Response: " + response);
    }

    /*
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }
    */
}

