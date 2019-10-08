package com.hico.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

import com.hico.models.Teacher;

import java.util.List;

@Repository
public interface TeacherRepository extends MongoRepository<Teacher, String> {

    Teacher       findByUserId(String id);
    Teacher       findByIdAndSchoolId(String id, String schoolId);
    Teacher       findByUserIdAndSchoolId(String userId, String schoolId);
    List<Teacher> findBySchoolId(String schoolId);

}
