package com.lithouse.common.dao;

import java.util.List;

import com.lithouse.common.model.DeviceItem;
import com.lithouse.common.model.GroupItem;


public interface DeviceDao extends GenericDao {
	public List < DeviceItem > createDevices ( String developerId, String groupId, int requestedDevicecount );
	public List < DeviceItem > getAllDevices ( String developerId, String groupId );
	public List < DeviceItem > createGroupsWithDevices ( GroupItem groupItem, int requestedDeviceCount );
}
