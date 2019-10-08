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

public class TeacherTest {

    /*
    private static String admin_login_chs = "{\n" +
        " \"emailId\": \"foo-1@chs.fuhsd.com\"," +
        " \"password\": \"hico123\"" +
        "}";

    private static String admin_login_lhs = "{\n" +
        " \"emailId\": \"baz-2@lhs.fuhsd.com\"," +
        " \"password\": \"hico123\"" +
        "}";
        */

    @DataProvider(name="createTeacherData")
    public Object[][] createTeacherData() {
        return new Object[][] {
            { TestData.school_1_admin_login_1, TestData.register_teacher_chs_1 },
                { TestData.school_1_admin_login_1, TestData.register_teacher_chs_2 },
                { TestData.school_1_admin_login_1, TestData.register_teacher_chs_3 },
                { TestData.school_1_admin_login_1, TestData.register_teacher_chs_4 },
                { TestData.school_2_admin_login_2, TestData.register_teacher_lhs_1 },
                { TestData.school_2_admin_login_2, TestData.register_teacher_lhs_2 },
                { TestData.school_2_admin_login_2, TestData.register_teacher_lhs_3 },
                { TestData.school_2_admin_login_2, TestData.register_teacher_lhs_4 },
        };
    }

    @DataProvider(name="adminLoginData")
    public Object[][] adminLogin() {
        return new Object[][] {
            { TestData.school_1_admin_login_1 },
            { TestData.school_2_admin_login_2 }
        };
    }


    @DataProvider(name="teacherLoginRegister")
    public Object[][] teacherLoginRegister() {
        return new Object[][] {
            { TestData.teacher_login_chs_1, TestData.register_teacher_chs_1 },
                { TestData.teacher_login_chs_2, TestData.register_teacher_chs_2 },
                { TestData.teacher_login_chs_3, TestData.register_teacher_chs_3 },
                { TestData.teacher_login_chs_4, TestData.register_teacher_chs_4 },
                { TestData.teacher_login_lhs_1, TestData.register_teacher_lhs_1 },
                { TestData.teacher_login_lhs_2, TestData.register_teacher_lhs_2 },
                { TestData.teacher_login_lhs_3, TestData.register_teacher_lhs_3 },
                { TestData.teacher_login_lhs_4, TestData.register_teacher_lhs_4 },
        };
    }



    @Test(dataProvider="createTeacherData", alwaysRun=true)
    public void createTeacherTest(String login, String payload) {

        String token = 
            given().
            contentType("application/json").
            body(login).
            when().
            post("http://localhost:8080/api/auth/login").
            then().
            log().body().
            statusCode(200).
            extract().
            path("token");

        given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            body(payload).
            when().
            post("http://localhost:8080/api/teacher/").
            then().
            log().body().
            statusCode(200);

    }

    @Test(dataProvider="teacherLoginRegister", alwaysRun=true)
    public void teacherLoginRegisterTest(String login, String payload) {

        Response resp =
            given().
            contentType("application/json").
            body(login).
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

        given().
        contentType("application/json").
        body(payload).
        header("Authorization", "Bearer " + token).
        when().
        put("http://localhost:8080/api/auth/pending/" + userId).
        then().
        log().body().
        statusCode(200);
    }

    @Test(dataProvider="adminLoginData", alwaysRun=true)
    public void listTeachersTest(String login) {

        String token = 
            given().
            contentType("application/json").
            body(login).
            when().
            post("http://localhost:8080/api/auth/login").
            then().
            log().body().
            statusCode(200).
            extract().
            path("token");

            given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            body(login).
            when().
            get("http://localhost:8080/api/teacher/").
            then().
            log().body().
            statusCode(anyOf(is(200), is(230))).
            extract();
    }
}


