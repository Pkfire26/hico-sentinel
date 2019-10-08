
package com.hico;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
//import org.gradle.api.tasks.testing.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import org.json.*;

public class ClubTest {

    // teacher creates club, assigns club supervisors and club admins
    // students register with clubs
    // students un-register with clubs
    // teacher deletes the club

    @DataProvider(name="clubCreateInfo")
    public Object[][] clubCreateInfo() {

        return new Object[][] {
         {
             TestData.teacher_login_chs_1,
             TestData.club_name_1, new String[]{},
             new String[] { "deepak.tijori@chs.fuhsd.org", "sam.houston@chs.fuhsd.org" }},
         {TestData.teacher_login_lhs_1,TestData.club_name_1, new String[]{},
             new String[]{ "ana.shiong@lhs.fuhsd.org", "xing.chi@lhs.fuhsd.org"}},
         {TestData.teacher_login_chs_2,TestData.club_name_2, new String[]{},
             new String[]  { "kui.zhang@chs.fuhsd.org", "deeoak.tijori@chs.fuhsd.org" }},
         {TestData.teacher_login_lhs_2,TestData.club_name_2, new String[]{},
             new String[] { "xing.chi@lhs.fuhsd.org" }},

         {TestData.teacher_login_chs_3,TestData.club_name_3, new String[]{},
             new String[] { "mike.tang@chs.fuhsd.org", "sam.houston@chs.fuhsd.org" }},
         {TestData.teacher_login_lhs_3,TestData.club_name_3, new String[]{},
             new String[] { "sampat.dhawan@lhs.fuhsd.org" }},
         {TestData.teacher_login_chs_2,TestData.club_name_4, new String[]{},
             new String []{ "venkat.ramana@chs.fuhsd.org" }},
         {TestData.teacher_login_lhs_2,TestData.club_name_4, new String[]{}, 
             new String[]{ "miguel.jose@lhs.fuhsd.org" }},
         {TestData.teacher_login_chs_4,TestData.club_name_5, new String[]{},
             new String[] {"kui.zhang@chs.fuhsd.org" }},
         {TestData.teacher_login_lhs_4,TestData.club_name_5, new String[]{},
             new String[] {"ana.shiong@lhs.fuhsd.org" }},

         {TestData.teacher_login_chs_1,TestData.club_name_6, new String[]{},
             new String[]{ "mike.tang@chs.fuhsd.org" }},
         {TestData.teacher_login_lhs_1,TestData.club_name_6, new String[]{},
            new String[]{ "jospeh.manuel@lhs.fuhsd.org" }}
         };
    }

    @DataProvider(name="studentClubRegister")
    public Object[][] studentClubRegister() {

        return new Object[][] {
            {TestData.register_student_chs_1, "Math" },
                {TestData.register_student_chs_2, "Rant" },
                {TestData.register_student_chs_3, "Rant" },
                {TestData.register_student_chs_5, "Rant" },
                {TestData.register_student_chs_6, "Rant" },
                {TestData.register_student_chs_1, "Rant" },

                {TestData.register_student_chs_3, "Math" },
                {TestData.register_student_chs_6, "Math" },
                {TestData.register_student_chs_5, "Math" },
                {TestData.register_student_chs_3, "Math" },
                {TestData.register_student_chs_2, "Math" },

                {TestData.register_student_lhs_3, "Math" },
                {TestData.register_student_lhs_6, "Math" },
                {TestData.register_student_lhs_5, "Math" },
                {TestData.register_student_lhs_3, "Math" },
                {TestData.register_student_lhs_2, "Math" },

                {TestData.register_student_chs_3, "Robotics" },
                {TestData.register_student_chs_6, "Robotics" },
                {TestData.register_student_chs_4, "Robotics" },
                {TestData.register_student_chs_3, "Robotics" },
                {TestData.register_student_chs_2, "Robotics" },

                {TestData.register_student_lhs_1, "Robotics" },
                {TestData.register_student_lhs_6, "Robotics" },
                {TestData.register_student_lhs_5, "Robotics" },
                {TestData.register_student_lhs_3, "Robotics" },
                {TestData.register_student_lhs_2, "Robotics" },

                {TestData.register_student_chs_3, "FBLA" },
                {TestData.register_student_chs_6, "FBLA" },
                {TestData.register_student_chs_4, "FBLA" },
                {TestData.register_student_chs_3, "FBLA" },
                {TestData.register_student_chs_2, "FBLA" },

                {TestData.register_student_lhs_1, "FBLA" },
                {TestData.register_student_lhs_6, "FBLA" },
                {TestData.register_student_lhs_5, "FBLA" },
                {TestData.register_student_lhs_3, "FBLA" },
                {TestData.register_student_lhs_2, "FBLA" },

                {TestData.register_student_chs_3, "Speech And Debate" },
                {TestData.register_student_chs_6, "Speech And Debate" },
                {TestData.register_student_chs_4, "Speech And Debate" },
                {TestData.register_student_chs_3, "Speech And Debate" },
                {TestData.register_student_chs_2, "Speech And Debate" },

                {TestData.register_student_lhs_1, "Speech And Debate" },
                {TestData.register_student_lhs_6, "Speech And Debate" },
                {TestData.register_student_lhs_5, "Speech And Debate" },
                {TestData.register_student_lhs_3, "Speech And Debate" },
                {TestData.register_student_lhs_2, "Speech And Debate" },


                {TestData.register_student_chs_6, "Science Olympiad" },
                {TestData.register_student_chs_4, "Science Olympiad" },
                {TestData.register_student_chs_3, "Science Olympiad" },
                {TestData.register_student_chs_2, "Science Olympiad" },
                {TestData.register_student_chs_5, "Science Olympiad" },

                {TestData.register_student_lhs_1, "Science Olympiad" },
                {TestData.register_student_lhs_6, "Science Olympiad" },
                {TestData.register_student_lhs_5, "Science Olympiad" },
                {TestData.register_student_lhs_3, "Science Olympiad" },
                {TestData.register_student_lhs_2, "Science Olympiad" },
        };
    }


    @DataProvider(name="listClubMembers")
    public Object[][] listClubMembers() {

        return new Object[][] {
            {TestData.school_1_admin_login_1, "Math" }
        };
    }

    @Test(dataProvider="clubCreateInfo", alwaysRun=true)
    public void createClubTest(String teacher, String clubName, String[] supervisors,
            String[] admins) {

        JSONObject obj = new JSONObject();
        obj.put("name", clubName);
        obj.put("supervisors", supervisors);
        obj.put("clubAdmins", admins);
        System.out.println(obj);

        String token =
            given().
            contentType("application/json").
            body(teacher).
            when().
            post("http://localhost:8080/api/auth/login").
            then().
            log().body().
            statusCode(200).
            extract().
            path("token");

        Response response = 
            given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            body(obj.toString()).
            when().
            post("http://localhost:8080/api/club/").
            then().
            log().body().
            statusCode(200).
            extract().
            response();
    }

    @Test(dataProvider="studentClubRegister", alwaysRun=true)
    public void studentClubRegisterTest(String student, String clubName) {

        JSONObject obj = new JSONObject(student);

        JSONObject login = new JSONObject();
        login.put("emailId", obj.get("emailId"));
        login.put("password", obj.get("password"));

        System.out.println("Login " + login.toString());

        Response resp =
            given().
            contentType("application/json").
            body(login.toString()).
            log().body().
            when().
            post("http://localhost:8080/api/auth/login").
            then().
            log().body().
            statusCode(200).
            extract().
            response();

        String userId = resp.jsonPath().get("userId");
        String token = resp.jsonPath().get("token");

         resp =
            given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            queryParam("name", clubName).
            when().
            get("http://localhost:8080/api/club").
            then().
            log().body().
            statusCode(200).
            extract().
            response();

        String clubId = resp.jsonPath().get("[0].id");
        System.out.println("ClubId: " + clubId);

        JSONObject register = new JSONObject();
        register.put("clubId", clubId);
        register.put("isMember", true);

        resp =
            given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            when().
            body(register.toString()).
            post("http://localhost:8080/api/club/" + clubId + "/register").
            then().
            log().body().
            statusCode(anyOf(is(200), is(409))).
            extract().
            response();

        // list user details.
        resp =
            given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            when().
            get("http://localhost:8080/api/student/" + userId).
            then().
            log().body().
            statusCode(200).
            extract().
            response();
    }



    @Test(dataProvider="listClubMembers", alwaysRun=true)
    public void listClubMembersTest(String schoolAdmin, String clubName) {

        String token =
            given().
            contentType("application/json").
            body(schoolAdmin).
            log().body().
            when().
            post("http://localhost:8080/api/auth/login").
            then().
            log().body().
            statusCode(200).
            extract().
            path("token");

        Response resp =
            given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            queryParam("name", clubName).
            when().
            get("http://localhost:8080/api/club").
            then().
            log().body().
            statusCode(200).
            extract().
            response();

        String clubId = resp.jsonPath().get("[0].id");
        System.out.println("ClubId: " + clubId);

        resp =
            given().
            contentType("application/json").
            header("Authorization", "Bearer " + token).
            when().
            get("http://localhost:8080/api/club/" + clubId + "/list").
            then().
            log().body().
            statusCode(anyOf(is(200), is(409))).
            extract().
            response();
    }
}
