package com.lithouse.common.dao.impl;


import java.util.List;

import com.lithouse.common.dao.GroupDao;
import com.lithouse.common.model.GroupItem;
import com.lithouse.common.model.GroupKeyItem;

public class GroupDaoImpl extends GenericDaoImpl implements GroupDao {

	@Override
	public List < GroupItem > getAllGroups ( String developerId ) {
		return queryItems ( GroupItem.class, new GroupItem ( developerId ) );
	}

	@Override
	public GroupItem createGroup ( GroupItem groupItem ) {
		GroupItem verifiedGroup = new GroupItem ( groupItem ); 
		GroupKeyItem groupKey = new GroupKeyItem ( groupItem.getDeveloperId ( ) );
		save ( groupKey );
		
		verifiedGroup.setGroupId ( groupKey.getGroupId ( ) );
		verifiedGroup.setGroupKey ( groupKey.getGroupKey ( ) );
		
		return save ( verifiedGroup ) ;
	}

}
