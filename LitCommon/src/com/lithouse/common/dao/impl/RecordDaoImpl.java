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
import com.lithouse.common.model.PermessionItem;
import com.lithouse.common.model.LatestRecordToDeviceItem;
import com.lithouse.common.model.Schema;
import com.lithouse.common.util.Global;

public class RecordDaoImpl extends GenericDaoImpl implements RecordDao {

	@Override
	public LatestRecordFromDeviceItem saveRecordFromDevice ( LatestRecordFromDeviceItem recordFromDevice ) {
		
		if ( recordFromDevice.getChannel ( ) == null 
				|| recordFromDevice.getChannel ( ).isEmpty ( ) ) {
			throw new IllegalArgumentException ( );
		}
		
		verifySourceDevice ( recordFromDevice );
		recordFromDevice.setTimeStamp ( Global.getCurrentTimestamp ( ) );
		recordFromDevice.setRangeKey ( recordFromDevice.getDeviceId ( ) 
				+ "#"  +  recordFromDevice.getChannel ( ) );
		
		//TODO: Save historical record
		return save ( recordFromDevice );
	}
	
	@Override
	public void saveRecordsToDevices ( List < LatestRecordToDeviceItem > records, 
					String appId, String groupId, String appDeveloperId ) {		
		verifyAppAccessToGroup ( appId, groupId, GroupItem.Type.READ_WRITE );
		verifyTargetDevices ( records, groupId ); 
		prepareRecordsForWriting ( records, appId, groupId );
		
		mapper.batchSave ( records );
	}

	@Override
	public void saveRecordToGroup ( LatestRecordToDeviceItem records, 
					String appId, String groupId, String appDeveloperId ) {
		// TODO Auto-generated method stub
	}

	private void verifyAppAccessToGroup ( String appId, String groupId, String accessType ) {
		if ( appId == null || groupId == null ) {
			throw new IllegalArgumentException ( "'appId' or 'groupId' or cannot be null" );
		}
		
		PermessionItem permession = find ( PermessionItem.class, appId, groupId );
		//TODO: clean up access comparison
		if ( null == permession || permession.getAccessType ( ) == null
				|| !permession.getAccessType ( ).contains ( accessType ) ) {
			throw new SecurityException ( "app does not have '" + accessType + "' access to this group" );
		}
		
	} 
	
	private DeviceItem verifySourceDevice ( LatestRecordFromDeviceItem recordFromDevice ) {
		if ( recordFromDevice.getGroupId ( ) == null || recordFromDevice.getGroupId ( ).isEmpty ( ) ) {
			throw new SecurityException ( "'groupId' is missing" );
		}
		
		if ( recordFromDevice.getDeviceId ( ) == null || recordFromDevice.getDeviceId ( ).isEmpty ( ) ) {
			throw new SecurityException ( "'deviceId' is missing" );
		}
		
		DeviceItem device = find ( DeviceItem.class, 
				recordFromDevice.getGroupId ( ), recordFromDevice.getDeviceId ( ) );
		
		if ( device == null ) {
			throw new SecurityException ( "Invalid 'deviceId'" );
		}
		
		return device;
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
	
	private void prepareRecordsForWriting ( List < LatestRecordToDeviceItem > records, String appId, String groupId ) {
		String timeStamp = Global.getCurrentTimestamp ( );
		
		for ( LatestRecordToDeviceItem record : records ) {
			record.setAppId ( appId );
			record.setGroupId ( groupId );
			record.setTimeStamp ( timeStamp );
		}
	}

	@Override
	public List < LatestRecordFromDeviceItem > readLatestRecordsFromDevices (
			List < String > deviceIds, List < String > channels, String appId,
			String groupId, String appDeveloperId ) {
		verifyAppAccessToGroup ( appId, groupId, GroupItem.Type.READ );
		
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
}
