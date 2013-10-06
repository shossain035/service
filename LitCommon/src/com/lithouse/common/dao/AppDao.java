package com.lithouse.common.dao;


import java.util.List;

import com.lithouse.common.model.AppItem;
import com.lithouse.common.model.PermessionItem;


public interface AppDao extends GenericDao {
	public List < AppItem > getAllApps ( String developerId );
	public AppItem createApp ( AppItem AppItem );
	public List < PermessionItem > addPermessionsToApp ( String appId, 
			String developerId, List < String > groupIds );
}
