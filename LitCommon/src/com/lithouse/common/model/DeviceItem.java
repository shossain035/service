package com.lithouse.common.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement ( name = "device" )
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.Device.tableName )
public class DeviceItem extends BaseModel {
	private String deviceId;
	private String deviceKey;
	@XmlTransient
	private String groupId;
	
	public DeviceItem ( ) { }
	public DeviceItem ( String groupId ) { 
		setGroupId ( groupId );
	}
	public DeviceItem ( String groupId, String deviceId, String deviceKey ) { 
		this ( groupId );
		setDeviceId ( deviceId );
		setDeviceKey ( deviceKey );
	}
	
	@DynamoDBRangeKey ( attributeName = Schema.Device.deviceId )
	public String getDeviceId () {
		return deviceId;
	}
	public void setDeviceId ( String deviceId ) {
		this.deviceId = deviceId;
	}
	@DynamoDBHashKey ( attributeName = Schema.Device.groupId )
	public String getGroupId () {
		return groupId;
	}
	public void setGroupId ( String groupId ) {
		this.groupId = groupId;
	}
	@DynamoDBAttribute ( attributeName = Schema.Device.deviceKey )
	public String getDeviceKey () {
		return deviceKey;
	}
	public void setDeviceKey ( String deviceKey ) {
		this.deviceKey = deviceKey;
	}

}

