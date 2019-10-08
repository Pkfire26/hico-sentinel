package com.hico.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

import java.util.HashSet;
import com.hico.models.ClubAssociation;

@Repository
public interface ClubAssociationRepository extends MongoRepository<ClubAssociation, String> {
    public HashSet<ClubAssociation> findAllByClubId(String clubId);
    public HashSet<ClubAssociation> findAllByUserId(String userId);
    public HashSet<ClubAssociation> findAllByClubIdAndStatus(String clubId, String status);
    public ClubAssociation findByClubIdAndUserId(String clubId, String userId);
}
