package com.lithouse.common.dao;


import java.util.List;

import com.lithouse.common.model.RecordToDevice;


public interface RecordDao extends GenericDao {
	//public List < RecordFromDevice > readLatestRecordsFromDevices ( String developerId );
	void saveRecordsToDevices ( List < RecordToDevice > records, String appId, String groupId, String appDeveloperId );
	void saveRecordToGroup ( RecordToDevice records, String appId, String groupId, String appDeveloperId );
}
