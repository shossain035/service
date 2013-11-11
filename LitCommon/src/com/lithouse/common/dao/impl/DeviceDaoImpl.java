package com.lithouse.common.dao.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.lithouse.common.dao.DeviceDao;
import com.lithouse.common.model.DeveloperItem;
import com.lithouse.common.model.DeviceKeyItem;
import com.lithouse.common.model.GroupItem;
import com.lithouse.common.model.GroupKeyItem;
import com.lithouse.common.model.GroupItem.Type;
import com.lithouse.common.model.DeviceItem;

public class DeviceDaoImpl extends GenericDaoImpl implements DeviceDao {

	@Override
	public List < DeviceItem > getAllDevices ( String developerId, String groupId ) {
		verifyGroupOwner ( developerId, groupId );
		return queryItems ( DeviceItem.class, new DeviceItem ( groupId ) );
	}
	
	@Override
	public List < DeviceItem > createDevices ( String developerId,
											   String groupId, 
											   int requestedDevicecount ) {
		
		GroupItem verifiedGroup = verifyGroupOwner ( developerId, groupId );
		updateDeveloperDeviceCount ( verifiedGroup, requestedDevicecount );
		
		return saveDevices ( verifiedGroup, requestedDevicecount );
	}
	
	private List < DeviceItem > saveDevices ( GroupItem group, int requestedDevicecount ) {
		List < Object > objectsToSave = new ArrayList < Object > (  );
		List < DeviceItem > devices = new ArrayList < DeviceItem > (  );
		for ( int i = 0; i < requestedDevicecount; i++ ) {
			DeviceItem device = new DeviceItem ( 
					group.getGroupId ( ), UUID.randomUUID ( ).toString ( ), UUID.randomUUID ( ).toString ( ) );
			devices.add ( device );
			
			objectsToSave.add ( device );			
			objectsToSave.add ( 
					new DeviceKeyItem ( 
							device.getGroupId ( ), device.getDeviceId ( ), group.getDeveloperId ( ), device.getDeviceKey ( ) ) );
		}
		
		//TODO: retry
		mapper.batchSave ( objectsToSave );
		
		return devices;
	}
//	private void saveDevicesWithRetry ( List < DeviceItem > devices ) {
//		mapper.batchSave ( devices );
//		
//		while ( true ) {
//			List < DeviceItem > failedDevices = new ArrayList < DeviceItem > ( );
//			for ( DeviceItem device : devices ) {
//				if ( device.getDeviceId ( ) == null || device.getDeviceId ( ).isEmpty ( ) ) {
//					failedDevices.add ( device );
//				}
//			}
//			
//			if ( failedDevices.isEmpty ( ) ) {
//				return;
//			}
//			mapper.batchSave ( failedDevices );
//		}
//	}
	
	private void updateDeveloperDeviceCount ( GroupItem group, int requestedDevicecount ) {
		DeveloperItem developer = find ( DeveloperItem.class, group.getDeveloperId ( ) );
		int updatedDeviceCount = requestedDevicecount + developer.getDeviceCount ( );
	    
		if ( updatedDeviceCount > developer.getDeviceLimit ( ) ) {
			throw new IllegalArgumentException (
						+ ( developer.getDeviceLimit ( ) - developer.getDeviceCount ( ) ) + " device(s) left" );
		}
		
		developer.setDeviceCount ( updatedDeviceCount );
		if ( Type.READ_WRITE.equalsIgnoreCase ( group.getType ( ) ) ) {
			developer.setWriteDeviceCount ( requestedDevicecount + developer.getWriteDeviceCount ( ) );
		}
		
		try {
	        mapper.save ( developer );
	    } catch ( ConditionalCheckFailedException e ) {
	        // Another process updated this item after we loaded it, so try again with the newest data
	    	updateDeveloperDeviceCount ( group, requestedDevicecount );			
	    }
	}
	
	private GroupItem verifyGroupOwner ( String developerId, String groupId ) {
		GroupItem groupItem = find ( GroupItem.class, developerId, groupId );
		
		if ( groupItem == null ) {
			throw new SecurityException ( );
		}
		
		return groupItem;
	}
	
	@Override
	public List < DeviceItem > createGroupsWithDevices ( GroupItem groupItem, int requestedDeviceCount ) {
		
		GroupItem verifiedGroup = new GroupItem ( groupItem ); 
		updateDeveloperDeviceCount ( groupItem, requestedDeviceCount );
		
		GroupKeyItem groupKey = new GroupKeyItem ( groupItem.getDeveloperId ( ) );
		save ( groupKey );
		
		verifiedGroup.setGroupId ( groupKey.getGroupId ( ) );
		verifiedGroup.setGroupKey ( groupKey.getGroupKey ( ) );
		
		save ( verifiedGroup ) ;
		
		return saveDevices ( verifiedGroup, requestedDeviceCount );
	}
}
