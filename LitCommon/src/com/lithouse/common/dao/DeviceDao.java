package com.lithouse.common.dao;

import java.util.List;

import com.lithouse.common.model.DeviceItem;


public interface DeviceDao extends GenericDao {
	public List < DeviceItem > createDevices ( String developerId, String groupId, int requestedDevicecount );
	public List < DeviceItem > getAllDevices ( String developerId, String groupId ); 
}
