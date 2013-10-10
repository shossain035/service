package com.lithouse.common.dao;


import java.util.List;

import com.lithouse.common.model.LatestRecord;
import com.lithouse.common.model.RecordToDevice;


public interface RecordDao extends GenericDao {
	public List < LatestRecord > readLatestRecordsFromDevices ( 
			List < String > deviceIds, List < String > channels, String appId, String groupId, String appDeveloperId );
	void saveRecordsToDevices ( List < RecordToDevice > records, String appId, String groupId, String appDeveloperId );
	void saveRecordToGroup ( RecordToDevice records, String appId, String groupId, String appDeveloperId );
	LatestRecord saveRecordFromDevice ( LatestRecord recordFromDevice );
}
