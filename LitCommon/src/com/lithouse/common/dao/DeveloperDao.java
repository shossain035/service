package com.lithouse.common.dao;

import com.lithouse.common.model.DeveloperItem;


public interface DeveloperDao extends GenericDao {
	DeveloperItem findWithIFTTTVerification ( String developerId );
	DeveloperItem updateDeveloper ( DeveloperItem developerItem );
}
