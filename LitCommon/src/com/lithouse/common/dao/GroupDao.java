package com.lithouse.common.dao;

import java.util.List;

import com.lithouse.common.model.GroupItem;


public interface GroupDao extends GenericDao {
	public List < GroupItem > getAllGroups ( String developerId );
	public GroupItem createGroup ( GroupItem groupItem );
}
