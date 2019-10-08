package com.hico.customrepo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.parser.Part;

import com.hico.models.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherRepositoryImpl implements TeacherRepositoryCustom  {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TeacherRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Teacher> findAllTeachersForSchool(String id) {

        Query query = new Query();
        query.addCriteria(Criteria.where("schoolId").is(id));

        return mongoTemplate.find(query, Teacher.class);
    }
}

