
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

import org.json.*;

public class StudentTest {

    // register student
    // school list - search for school name / zip to get the school Id;
    // register with the school Id and RegCode


    @DataProvider(name="studentData")
    public Object[][] studentData() {


        return new Object[][] {
                { TestData.register_student_chs_1 }
        };
    }


    @DataProvider(name="schoolInfo")
    public Object[][] schoolInfo() {
        return new Object[][] {
            { TestData.school_lookup_1, TestData.school_reg_code_1, TestData.register_student_chs_1  },
            { TestData.school_lookup_1, TestData.school_reg_code_1, TestData.register_student_chs_2  },
            { TestData.school_lookup_1, TestData.school_reg_code_1, TestData.register_student_chs_3  },
            { TestData.school_lookup_1, TestData.school_reg_code_1, TestData.register_student_chs_4  },
            { TestData.school_lookup_1, TestData.school_reg_code_1, TestData.register_student_chs_5  },
            { TestData.school_lookup_1, TestData.school_reg_code_1, TestData.register_student_chs_6  },
            { TestData.school_lookup_2, TestData.school_reg_code_2, TestData.register_student_lhs_1  },
            { TestData.school_lookup_2, TestData.school_reg_code_2, TestData.register_student_lhs_2  },
            { TestData.school_lookup_2, TestData.school_reg_code_2, TestData.register_student_lhs_3  },
            { TestData.school_lookup_2, TestData.school_reg_code_2, TestData.register_student_lhs_4  },
            { TestData.school_lookup_2, TestData.school_reg_code_2, TestData.register_student_lhs_5  },
            { TestData.school_lookup_2, TestData.school_reg_code_2, TestData.register_student_lhs_6  }
        };
    }


    @Test(dataProvider="schoolInfo", alwaysRun=true)
    public void createStudentTest(String payload, String regCode, String studentInfo) {

        String id =
        given().
            contentType("application/json").
            body(payload).
        when().
            get("http://localhost:8080/api/auth/lookup/schools").
            then().
            log().body().
            statusCode(200).
            extract().
            path("[0].id");


        System.out.println("ID: " + id);
        JSONObject obj = new JSONObject(studentInfo);
        obj.put("schoolId", id);
        obj.put("regCode", regCode);

        Response response = 
        given().
            contentType("application/json").
            body(obj.toString()).
            when().
            post("http://localhost:8080/api/auth/register").
            then().
            log().body().
            statusCode(200).
            extract().
            response();

    }
}

