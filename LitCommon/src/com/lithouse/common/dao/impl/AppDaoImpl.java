package com.lithouse.common.dao.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.lithouse.common.dao.AppDao;
import com.lithouse.common.model.AppItem;
import com.lithouse.common.model.AppKeyItem;
import com.lithouse.common.model.GroupItem;
import com.lithouse.common.model.PermessionItem;
import com.lithouse.common.model.Schema;

public class AppDaoImpl extends GenericDaoImpl implements AppDao {

	@Override
	public List < AppItem > getAllApps ( String developerId ) {
		return queryItems ( AppItem.class, new AppItem ( developerId ) );
	}

	@Override
	public AppItem createApp ( AppItem appItem ) {
		AppItem verifiedApp = new AppItem ( appItem );
		AppKeyItem appKey = new AppKeyItem ( appItem.getDeveloperId ( ) );
		save ( appKey );
		
		verifiedApp.setAppId ( appKey.getAppId ( ) );
		verifiedApp.setAppKey ( appKey.getAppKey ( ) );
		
		return save ( verifiedApp );
	}

	@Override
	public List < PermessionItem > addPermessionsToApp ( String appId, String developerId, List < String > groupIds ) {
		List < Object > verifiedGroups = verifyGroupAccess ( developerId, groupIds );
		
		List < PermessionItem > permessions = new ArrayList < PermessionItem > ( );
		for ( Object group : verifiedGroups ) {
			GroupItem groupItem = ( GroupItem ) group;
			permessions.add ( new PermessionItem ( 
					appId, groupItem.getGroupId ( ), groupItem.getType ( ) ) );			
		}
		//TODO: Retry
		mapper.batchSave ( permessions );
		return queryItems ( PermessionItem.class, new PermessionItem ( appId ) );
	}
	
	private List < Object > verifyGroupAccess ( String developerId, List < String > groupIds ) {
		
		List < Object > matchedGoups = getMatchedGroups ( developerId, groupIds );
		
		//TODO: Consider public groups by others. Consider same group mentioned more than once
		if ( matchedGoups == null || matchedGoups.size ( ) != groupIds.size ( ) ) {
			throw new SecurityException ( );
		}
		
		return matchedGoups;
	}
	
	private List < Object > getMatchedGroups ( String developerId, List < String > groupIds ) {
		
		List < KeyPair > keyPairs = new ArrayList < KeyPair > ( );	
		for ( String groupId : groupIds ) {
			keyPairs.add ( new KeyPair ( ).withHashKey ( developerId ).withRangeKey ( groupId ) );
		}
		
		Map < Class < ? >, List < KeyPair > > keyMap = new HashMap < Class < ? >, List < KeyPair > > (  );
		keyMap.put ( GroupItem.class, keyPairs );
		
		return mapper.batchLoad ( keyMap ).get ( Schema.Group.tableName );
	}
}
