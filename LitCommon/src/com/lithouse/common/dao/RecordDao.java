package com.lithouse.common.dao;


import java.util.List;

import com.lithouse.common.model.LatestRecordFromDeviceItem;
import com.lithouse.common.model.LatestRecordToDeviceItem;


public interface RecordDao extends GenericDao {
	List < LatestRecordFromDeviceItem > readLatestRecordsFromDevices ( 
			List < String > deviceIds, List < String > channels, String appId, String groupId, String appDeveloperId );
	void saveRecordsToDevices ( List < LatestRecordToDeviceItem > records, String appId, String groupId, String appDeveloperId );
	
	List < LatestRecordToDeviceItem > readLatestRecordsToDevice ( String groupId, String deviceId ); 
	void saveRecordsFromDevice ( 
			List < LatestRecordFromDeviceItem > records, String groupId, String deviceId );
}
