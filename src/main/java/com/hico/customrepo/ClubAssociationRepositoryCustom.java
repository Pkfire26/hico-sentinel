package com.hico.customrepo;

import java.util.List;
import com.hico.models.ClubAssociation;

public interface ClubAssociationRepositoryCustom {

	List<ClubAssociation> findByClubIdAndClubRolesContains(String clubId, String clubRole);

}

