package com.hico.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

import com.hico.models.User;
import com.hico.models.Role;
import java.util.List;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
    //public User findById(String id);
    public User findByEmailId(String id);
    // find a student using Id and School Id
    public User findByIdAndSchoolId(String id, String schoolId);

    public List<User> findByRolesAndSchoolId(Role role, String schoolId);
}
