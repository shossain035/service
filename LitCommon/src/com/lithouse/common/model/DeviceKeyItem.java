package com.lithouse.common.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


@DynamoDBTable ( tableName = Schema.DeviceKey.tableName )
public class DeviceKeyItem extends BaseModel {
	private String deviceKey;
	private String deviceId;
	private String groupId;
	private String developerId;
	private String ownerId;
	
	public DeviceKeyItem ( ) {	}
	
	public DeviceKeyItem ( String groupId, String deviceId, String developerId, String deviceKey ) {
		setGroupId ( groupId );
		setDeviceId ( deviceId );
		setDeveloperId ( developerId );
		setDeviceKey ( deviceKey );
	
		// On creation, device is owned by the developer
		setOwnerId ( developerId );
	}
	
	@DynamoDBHashKey ( attributeName = Schema.DeviceKey.deviceKey )	
	public String getDeviceKey ( ) {
		return deviceKey;
	}
	
	public void setDeviceKey ( String deviceKey ) {
		this.deviceKey = deviceKey;
	}

	@DynamoDBAttribute ( attributeName = Schema.DeviceKey.deviceId )
	public String getDeviceId () {
		return deviceId;
	}

	public void setDeviceId ( String deviceId ) {
		this.deviceId = deviceId;
	}

	@DynamoDBAttribute ( attributeName = Schema.DeviceKey.groupId )
	public String getGroupId () {
		return groupId;
	}

	public void setGroupId ( String groupId ) {
		this.groupId = groupId;
	}

	@DynamoDBAttribute ( attributeName = Schema.DeviceKey.developerId )
	public String getDeveloperId () {
		return developerId;
	}

	public void setDeveloperId ( String developerId ) {
		this.developerId = developerId;
	}

	@DynamoDBAttribute ( attributeName = Schema.DeviceKey.ownerId )
	public String getOwnerId () {
		return ownerId;
	}

	public void setOwnerId ( String ownerId ) {
		this.ownerId = ownerId;
	}

}

