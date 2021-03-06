package com.lithouse.common.dao.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.lithouse.common.dao.DBFilter;
import com.lithouse.common.dao.RecordDao;
import com.lithouse.common.model.DeviceItem;
import com.lithouse.common.model.GroupItem;
import com.lithouse.common.model.LatestRecordFromDeviceItem;
import com.lithouse.common.model.LatestRecordToDeviceItem;
import com.lithouse.common.model.Schema;
import com.lithouse.common.util.Global;

public class RecordDaoImpl extends GenericDaoImpl implements RecordDao {

	@Override
	public void saveRecordsFromDevice ( List < LatestRecordFromDeviceItem > records, String groupId, String deviceId ) {
		if ( records == null || groupId == null || deviceId == null ) {
			throw new IllegalArgumentException ( );
		} 
		String timestamp = Global.getCurrentTimestamp ( );
		
		for ( LatestRecordFromDeviceItem record : records ) {
			//TODO: Verify channel against group
			record.setTimeStamp ( timestamp );
			record.setGroupId ( groupId );
			record.setDeviceId ( deviceId );
			record.setRangeKey ( record.getDeviceId ( ) + "#"  +  record.getChannel ( ) );
			if ( record.getData ( ) == null ) {
				record.setData ( "" );
			}
		}
		
		//TODO: Save historical record
		mapper.batchSave ( records );
	}
	
	@Override
	public void saveRecordsToDevices ( List < LatestRecordToDeviceItem > records, 
					String appId, String groupId, String appDeveloperId, boolean isDebug ) {		
		if ( ! isDebug ) {
			verifyDeveloperOwenership ( appDeveloperId, groupId );
			verifyTargetDevices ( records, groupId );
		} 
		
		mapper.batchSave ( records );
	}

//	private void verifyAppAccessToGroup ( String appId, String groupId, String accessType ) {
//		if ( appId == null || groupId == null ) {
//			throw new IllegalArgumentException ( "'appId' or 'groupId' or cannot be null" );
//		}
//		
//		PermissionItem permission = find ( PermissionItem.class, appId, groupId );
//		//TODO: clean up access comparison
//		if ( null == permission || permission.getAccessType ( ) == null
//				|| !permission.getAccessType ( ).contains ( accessType ) ) {
//			throw new SecurityException ( "app does not have '" + accessType + "' access to this group" );
//		}
//		
//	}
	
	private void verifyDeveloperOwenership ( String developerId, String groupId ) {
		if ( developerId == null || groupId == null ) {
			throw new IllegalArgumentException ( "'developerId' or 'groupId' or cannot be null" );
		}
		
		GroupItem group = find ( GroupItem.class, developerId, groupId );
		//TODO: clean up access comparison
		if ( null == group ) {
			throw new SecurityException ( "app does not have access to this group" );
		}
		
	}
			
	private < T extends LatestRecordToDeviceItem > List < Object > verifyTargetDevices ( 
			List < LatestRecordToDeviceItem > records, String groupId ) {
		Set < String > deviceIds = new HashSet < String > ( );
		List < KeyPair > keyPairs = new ArrayList < KeyPair > ( );	
		
		for ( LatestRecordToDeviceItem record : records ) {
			// no need to hit db for same device
			if ( !deviceIds.contains ( record.getDeviceId ( ) ) ) {
				deviceIds.add ( record.getDeviceId ( ) );
				keyPairs.add ( new KeyPair ( ).withHashKey ( groupId ).withRangeKey ( record.getDeviceId ( ) ) );
			}
			//replacing null data with empty string
			if ( record.getData ( ) == null ) {
				record.setData ( "" );
			}
		}
		
		Map < Class < ? >, List < KeyPair > > keyMap = new HashMap < Class < ? >, List < KeyPair > > (  );
		keyMap.put ( DeviceItem.class, keyPairs );
		List < Object > targetDevices = mapper.batchLoad ( keyMap ).get ( Schema.Device.tableName );
		
		if ( targetDevices == null || targetDevices.size ( ) != deviceIds.size ( ) ) {
			throw new SecurityException ( "Make sure app has access to the group "
											+ "and all the deviceIds are from that group." );
		}
		return targetDevices;
	}
	
	@Override
	public List < LatestRecordFromDeviceItem > readLatestRecordsFromDevices (
			List < String > deviceIds, List < String > channels, String appId,
			String groupId, String appDeveloperId ) {
		verifyDeveloperOwenership ( appDeveloperId, groupId );
		
		if ( deviceIds == null || deviceIds.isEmpty ( ) ) {
			if ( channels == null || channels.isEmpty ( ) ) {
				return queryItems ( LatestRecordFromDeviceItem.class, new LatestRecordFromDeviceItem ( groupId ) );
			}
			return getLatestRecordsByChannels ( channels, groupId );
		} else {
			if ( channels == null || channels.isEmpty ( ) ) {
				return getLatestRecordsByDeviceIds ( deviceIds, groupId );
			} 
			return getLatestRecordsByDeviceIdsAndChannels ( deviceIds, channels, groupId );
		}		
	}
	
	private List < LatestRecordFromDeviceItem > getLatestRecordsByChannels ( List < String > channels,  String groupId ) {
		List < LatestRecordFromDeviceItem > results = new ArrayList < LatestRecordFromDeviceItem > ( );
		
		for ( String channel: channels ) {
			results.addAll ( queryItems ( 
					LatestRecordFromDeviceItem.class, new LatestRecordFromDeviceItem ( groupId ), 
					new DBFilter < String > ( 
							Schema.LatestRecordFromDevice.channel, channel, ComparisonOperator.EQ ) ) );
		}
		
		return results;
	}
		

	private List < LatestRecordFromDeviceItem > getLatestRecordsByDeviceIds ( List < String > deviceIds,  String groupId ) {
		List < LatestRecordFromDeviceItem > results = new ArrayList < LatestRecordFromDeviceItem > ( );
		
		for ( String deviceId: deviceIds ) {
			results.addAll ( queryItems ( 
					LatestRecordFromDeviceItem.class, new LatestRecordFromDeviceItem ( groupId ), 
					new DBFilter < String > ( 
							Schema.LatestRecordFromDevice.rangeKey, deviceId + "#", ComparisonOperator.BEGINS_WITH ) ) );
		}
		
		return results;
	}
	
	private List < LatestRecordFromDeviceItem > getLatestRecordsByDeviceIdsAndChannels ( 
							List < String > deviceIds, List < String > channels, String groupId ) {
		List < Object > itemsToGet = new ArrayList < Object > ( );
		
		for ( String deviceId : deviceIds ) {
			for ( String channel : channels ) {
				itemsToGet.add ( new LatestRecordFromDeviceItem ( groupId, deviceId + "#" + channel ) );
			} 
		}
		
		List < Object > dbResults = 
				mapper.batchLoad ( itemsToGet ).get ( Schema.LatestRecordFromDevice.tableName );
		List < LatestRecordFromDeviceItem > results = new ArrayList < LatestRecordFromDeviceItem > ( );
		
		if ( dbResults != null ) {
			for ( Object record : dbResults ) {
				results.add ( ( LatestRecordFromDeviceItem ) record );
			}
		}
		
		return results;
	}

	@Override
	public List < LatestRecordToDeviceItem > readLatestRecordsToDevice ( String deviceId ) {
		if ( deviceId == null ) {
			throw new IllegalArgumentException ( "'deviceId' cannot be null" );
		}
		
		List < LatestRecordToDeviceItem > results = queryItems ( 
				LatestRecordToDeviceItem.class, new LatestRecordToDeviceItem ( deviceId ) );
		
		for ( LatestRecordToDeviceItem record: results ) {
			//No need to send deviceId back to device
			record.setDeviceId ( null );
		}
		return results;
	}
}
