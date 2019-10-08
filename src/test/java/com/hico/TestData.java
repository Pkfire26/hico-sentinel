package com.hico;



public class TestData {

    public static String school_info_1 = "{\n" +
        " \"name\": \"Cupertino High School\"," +
        " \"address\": {" +
        " \"street\": \"10100 Finch Ave\"," +
        " \"city\": \"Cupertino\"," +
        " \"state\": \"CA\"," +
        " \"zipCode\": \"95014\"," +
        " \"country\": \"USA\"" +
        " }," +
        "\"phoneNos\": [\"408-366-7700\"]," +
        " \"schoolAdmins\": [" +
        "     \"foo-1@chs.fuhsd.org\"," +
        "     \"foo-2@chs.fuhsd.org\"" +
        " ]," +
        " \"regCode\": \"Dusty\"" +
        "}";

    public static String school_info_2 = "{\n" +
        " \"name\": \"Lynbrook High School\"," +
        " \"address\": {" +
        " \"street\": \"1280 Johnson Ave\"," +
        " \"city\": \"San Jose\","+
        " \"state\": \"CA\"," +
        " \"zipCode\": \"95129\"," +
        " \"country\": \"USA\"" +
        "}," +
        " \"phoneNos\": [\"408-366-7700\"]," +
        " \"schoolAdmins\": [" +
        "        \"baz-1@lhs.fuhsd.org\"," +
        "        \"baz-2@lhs.fuhsd.org\"" +
        "    ]," +
        " \"regCode\": \"Zingo\"" +
        "}";

    // school admins
    public static String school_1_admin_login_1 = "{\n" +
        " \"emailId\": \"foo-1@chs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String school_1_admin_login_2 = "{\n" +
        " \"emailId\": \"foo-2@chs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String school_2_admin_login_1 = "{\n" +
        " \"emailId\": \"baz-1@lhs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String school_2_admin_login_2 = "{\n" +
        " \"emailId\": \"baz-2@lhs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String register_payload_1 = "{\n" +
        " \"emailId\": \"foo-1@chs.fuhsd.org\"," +
        " \"firstName\": \"Foo-1\"," +
        " \"lastName\": \"Bar\"," +
        " \"cellPhoneNo\": \"222-111-1212\"" +
        "}";

    public static String register_payload_2 = "{\n" +
        " \"emailId\": \"foo-2@chs.fuhsd.org\"," +
        " \"firstName\": \"Foo-2\"," +
        " \"lastName\": \"Qux\"," +
        " \"cellPhoneNo\": \"222-333-1212\"" +
        "}";

    public static String register_payload_3 = "{\n" +
        " \"emailId\": \"baz-1@lhs.fuhsd.org\"," +
        " \"firstName\": \"Baz-1\"," +
        " \"lastName\": \"Quuz\"," +
        " \"cellPhoneNo\": \"222-444-1212\"" +
        "}";

    public static String register_payload_4 = "{\n" +
        " \"emailId\": \"baz-2@lhs.fuhsd.org\"," +
        " \"firstName\": \"Baz-1\"," +
        " \"lastName\": \"Bar\"," +
        " \"cellPhoneNo\": \"222-555-1212\"" +
        "}";

    // teacher registration
    public static String register_teacher_chs_1 = "{\n" +
        " \"emailId\": \"john.scully@chs.fuhsd.org\"," +
        " \"firstName\": \"John\"," +
        " \"lastName\": \"Scully\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-123-1212\"" +
        "}";

    public static String register_teacher_chs_2 = "{\n" +
        " \"emailId\": \"ann.miller@chs.fuhsd.org\"," +
        " \"firstName\": \"Ann\"," +
        " \"lastName\": \"Miller\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-123-2345\"" +
        "}";
    public static String register_teacher_chs_3 = "{\n" +
        " \"emailId\": \"mike.gunther@chs.fuhsd.org\"," +
        " \"firstName\": \"Mike\"," +
        " \"lastName\": \"Gunther\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-123-2321\"" +
        "}";

    public static String register_teacher_chs_4 = "{\n" +
        " \"emailId\": \"sam.murthy@chs.fuhsd.org\"," +
        " \"firstName\": \"Samish\"," +
        " \"lastName\": \"Murthy\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-123-2325\"" +
        "}";

    public static String register_teacher_lhs_1 = "{\n" +
        " \"emailId\": \"ram.sharma@lhs.fuhsd.org\"," +
        " \"firstName\": \"Ram\"," +
        " \"lastName\": \"Sharma\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-123-2211\"" +
        "}";

    public static String register_teacher_lhs_2 = "{\n" +
        " \"emailId\": \"mike.winters@lhs.fuhsd.org\"," +
        " \"firstName\": \"Mike\"," +
        " \"lastName\": \"Winters\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-123-2311\"" +
        "}";

    public static String register_teacher_lhs_3 = "{\n" +
        " \"emailId\": \"shanti.menon@lhs.fuhsd.org\"," +
        " \"firstName\": \"Shanti`\"," +
        " \"lastName\": \"Menon\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-145-2566\"" +
        "}";

    public static String register_teacher_lhs_4 = "{\n" +
        " \"emailId\": \"kulu.badshah@lhs.fuhsd.org\"," +
        " \"firstName\": \"Kulu\"," +
        " \"lastName\": \"Badshah\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"322-146-2314\"" +
        "}";


    // teacher login
    public static String teacher_login_chs_1 = "{\n" +
        " \"emailId\": \"john.scully@chs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String teacher_login_chs_2 = "{\n" +
        " \"emailId\": \"ann.miller@chs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String teacher_login_chs_3 = "{\n" +
        " \"emailId\": \"mike.gunther@chs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String teacher_login_chs_4 = "{\n" +
        " \"emailId\": \"sam.murthy@chs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String teacher_login_lhs_1 = "{\n" +
        " \"emailId\": \"ram.sharma@lhs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String teacher_login_lhs_2 = "{\n" +
        " \"emailId\": \"mike.winters@lhs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String teacher_login_lhs_3 = "{\n" +
        " \"emailId\": \"shanti.menon@lhs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    public static String teacher_login_lhs_4 = "{\n" +
        " \"emailId\": \"kulu.badshah@lhs.fuhsd.org\"," +
        " \"password\": \"hico123\"" +
        "}";

    //////////////////////////
    //Student Info
    
    public static String register_student_chs_1 = "{\n" +
        " \"emailId\": \"deepak.tijori@chs.fuhsd.org\"," +
        " \"firstName\": \"Deepak\"," +
        " \"lastName\": \"Tojori\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-123-1512\"" +
        "}";

    public static String register_student_chs_2 = "{\n" +
        " \"emailId\": \"sam.houston@chs.fuhsd.org\"," +
        " \"firstName\": \"Sam\"," +
        " \"lastName\": \"Houston\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-123-3212\"" +
        "}";

    public static String register_student_chs_3 = "{\n" +
        " \"emailId\": \"venkat.ramana@chs.fuhsd.org\"," +
        " \"firstName\": \"Venkat\"," +
        " \"lastName\": \"Ramana\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-128-1012\"" +
        "}";

    public static String register_student_chs_4 = "{\n" +
        " \"emailId\": \"kui.zhang@chs.fuhsd.org\"," +
        " \"firstName\": \"Kui\"," +
        " \"lastName\": \"Zhang\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-183-1212\"" +
        "}";

    public static String register_student_chs_5 = "{\n" +
        " \"emailId\": \"mike.tang@chs.fuhsd.org\"," +
        " \"firstName\": \"Mike\"," +
        " \"lastName\": \"Tang\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"702-123-1212\"" +
        "}";

    public static String register_student_chs_6 = "{\n" +
        " \"emailId\": \"samuel.adams@chs.fuhsd.org\"," +
        " \"firstName\": \"Samuel\"," +
        " \"lastName\": \"Adams\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-128-1292\"" +
        "}";

    public static String register_student_lhs_1 = "{\n" +
        " \"emailId\": \"ana.shiong@lhs.fuhsd.org\"," +
        " \"firstName\": \"Ana\"," +
        " \"lastName\": \"Shiong\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-183-1212\"" +
        "}";

    public static String register_student_lhs_2 = "{\n" +
        " \"emailId\": \"xing.chi@lhs.fuhsd.org\"," +
        " \"firstName\": \"Xing\"," +
        " \"lastName\": \"Chi\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"309-123-9212\"" +
        "}";

    public static String register_student_lhs_3 = "{\n" +
        " \"emailId\": \"sampat.dhawan@lhs.fuhsd.org\"," +
        " \"firstName\": \"Sampat\"," +
        " \"lastName\": \"Dhawan\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-129-1282\"" +
        "}";

    public static String register_student_lhs_4 = "{\n" +
        " \"emailId\": \"miguel.jose@lhs.fuhsd.org\"," +
        " \"firstName\": \"Miguel\"," +
        " \"lastName\": \"Jose\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-123-1878\"" +
        "}";

    public static String register_student_lhs_5 = "{\n" +
        " \"emailId\": \"shanti.pukkad@lhs.fuhsd.org\"," +
        " \"firstName\": \"Shanti\"," +
        " \"lastName\": \"Pukkad\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-129-1862\"" +
        "}";

    public static String register_student_lhs_6 = "{\n" +
        " \"emailId\": \"joseph.manuel@lhs.fuhsd.org\"," +
        " \"firstName\": \"Joseph\"," +
        " \"lastName\": \"Manuel\"," +
        " \"password\": \"hico123\"," +
        " \"cellPhoneNo\": \"302-193-1282\"" +
        "}";


    // School lookup
    public static String school_lookup_1 = "{\n" +
        " \"name\": \"Cupertino High School\"," +
        " \"zipCode\": \"95014\"" +
        "}";

    public static String school_lookup_2 = "{\n" +
        " \"name\": \"Lynbrook High School\"," +
        " \"zipCode\": \"95129\"" +
        "}";

    // School Codes
    public static String school_reg_code_1 = "Dusty";
    public static String school_reg_code_2 = "Zingo";


    public static String club_name_1 = "Speech And Debate";
    public static String club_name_2 = "FBLA";
    public static String club_name_3 = "Science Olympiad";
    public static String club_name_4 = "Robotics";
    public static String club_name_5 = "Rant";
    public static String club_name_6 = "Math";




}

