package com.hico.customrepo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.parser.Part;

import com.hico.models.ClubAssociation;

import java.util.ArrayList;
import java.util.List;

public class ClubAssociationRepositoryImpl implements ClubAssociationRepositoryCustom  {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ClubAssociationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ClubAssociation> findByClubIdAndClubRolesContains(String clubId, String clubRole) {

        Query query = new Query();
        query.addCriteria(Criteria.where("clubId").is(clubId));

        return mongoTemplate.find(query, ClubAssociation.class);
    }
}

