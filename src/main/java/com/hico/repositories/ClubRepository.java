package com.hico.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

import java.util.HashSet;
import com.hico.models.Club;

@Repository
public interface ClubRepository extends MongoRepository<Club, String> {
    //public Club findById(String id);
    //
    public HashSet<Club> findAllBySchoolId(String schoolId);
    public HashSet<Club> findAllByName(String name);
    public Club findByNameAndSchoolId(String name, String schoolId);
}
