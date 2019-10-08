package com.hico.repositories;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

import com.hico.models.School;
import com.hico.models.SchoolLookup;
import java.util.List;

@Repository
public interface SchoolRepository extends MongoRepository<School, String> {
    //@Query("SELECT t FROM School t where t.name = ?0 AND t.address.zipCode = ?1")
    //public School findSchool(String name, String zipCode);
    public School findByNameAndAddressZipCode(String name, String zipCode);
    public List<SchoolLookup> findByName(String name);
    public List<SchoolLookup> findByAddressZipCode(String zipCode);
}
