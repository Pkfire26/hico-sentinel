package com.hico.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

import com.hico.models.Student;
import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    public Student findByUserId(String userId);
    public Student findByUserIdAndSchoolId(String userId, String schoolId);
    public List<Student> findAllBySchoolId(String schoolId);
}
