package com.lithouse.common.dao;


import java.util.List;

import com.lithouse.common.model.AppItem;
import com.lithouse.common.model.PermissionItem;


public interface AppDao extends GenericDao {
	public List < AppItem > getAllApps ( String developerId );
	public AppItem createApp ( AppItem AppItem );
	public List < PermissionItem > addPermissionsToApp ( String appId, 
			String developerId, List < String > groupIds );
}
