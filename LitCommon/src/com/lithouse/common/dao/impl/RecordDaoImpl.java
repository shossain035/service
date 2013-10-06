package com.lithouse.common.dao.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.lithouse.common.dao.RecordDao;
import com.lithouse.common.model.DeviceItem;
import com.lithouse.common.model.GroupItem;
import com.lithouse.common.model.PermessionItem;
import com.lithouse.common.model.RecordToDevice;
import com.lithouse.common.model.Schema;
import com.lithouse.common.util.Global;

public class RecordDaoImpl extends GenericDaoImpl implements RecordDao {

	@Override
	public void saveRecordsToDevices ( List < RecordToDevice > records, 
					String appId, String groupId, String appDeveloperId ) {		
		verifyAppAccessToGroup ( appId, groupId );
		verifyTargetDevices ( records, groupId ); 
		prepareRecords ( records, appId, groupId );
		
		mapper.batchSave ( records );
	}

	@Override
	public void saveRecordToGroup ( RecordToDevice records, 
					String appId, String groupId, String appDeveloperId ) {
		// TODO Auto-generated method stub
	}

	private void verifyAppAccessToGroup ( String appId, String groupId ) {
		if ( appId == null || groupId == null ) {
			throw new IllegalArgumentException ( "'appId' or, 'groupId' or cannot be null" );
		}
		
		PermessionItem permession = find ( PermessionItem.class, appId, groupId );
		if ( null == permession || !GroupItem.Type.READ_WRITE.equals ( permession.getAccessType ( ) ) ) {
			throw new SecurityException ( "app does not have write access to this group" );
		}
		
	} 
	
	
	private List < Object > verifyTargetDevices ( List < RecordToDevice > records, String groupId ) {
		Set < String > deviceIds = new HashSet < String > ( );
		List < KeyPair > keyPairs = new ArrayList < KeyPair > ( );	
		
		for ( RecordToDevice record : records ) {
			// no need to hit db for same device
			if ( !deviceIds.contains ( record.getDeviceId ( ) ) ) {
				deviceIds.add ( record.getDeviceId ( ) );
				keyPairs.add ( new KeyPair ( ).withHashKey ( groupId ).withRangeKey ( record.getDeviceId ( ) ) );
			}
		}
		
		Map < Class < ? >, List < KeyPair > > keyMap = new HashMap < Class < ? >, List < KeyPair > > (  );
		keyMap.put ( DeviceItem.class, keyPairs );
		List < Object > targetDevices = mapper.batchLoad ( keyMap ).get ( Schema.Device.tableName );
		
		if ( targetDevices == null || targetDevices.size ( ) != deviceIds.size ( ) ) {
			throw new SecurityException ( "Make sure app has write access to the group "
											+ "and all the deviceIds are from that group." );
		}
		return targetDevices;
	}
	
	private void prepareRecords ( List < RecordToDevice > records, String appId, String groupId ) {
		String timeStamp = Global.getCurrentTimestamp ( );
		
		for ( RecordToDevice record : records ) {
			record.setAppId ( appId );
			record.setGroupId ( groupId );
			record.setTimeStamp ( timeStamp );
		}
	}
}
