package com.lithouse.common.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement ( name = "recordFrom" )
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.RecordFromDevice.tableName )
public class RecordFromDevice extends BaseModel  {
	
	private String deviceId;
	private String timeStamp;
	@XmlTransient
	private String groupId;
	private String channel;
	private String data;
	
	public RecordFromDevice ( ) {	}

	@DynamoDBHashKey ( attributeName = Schema.RecordFromDevice.deviceId )
	public String getDeviceId () {
		return deviceId;
	}

	public void setDeviceId ( String deviceId ) {
		this.deviceId = deviceId;
	}

	@DynamoDBRangeKey ( attributeName = Schema.RecordFromDevice.timeStamp )
	public String getTimeStamp () {
		return timeStamp;
	}

	public void setTimeStamp ( String timeStamp ) {
		this.timeStamp = timeStamp;
	}

	@DynamoDBAttribute ( attributeName = Schema.RecordFromDevice.groupId )
	public String getGroupId () {
		return groupId;
	}

	public void setGroupId ( String groupId ) {
		this.groupId = groupId;
	}

	@DynamoDBAttribute ( attributeName = Schema.RecordFromDevice.channel )
	public String getChannel () {
		return channel;
	}

	public void setDataHead ( String channel ) {
		this.channel = channel;
	}

	@DynamoDBAttribute ( attributeName = Schema.RecordFromDevice.data )
	public String getData () {
		return data;
	}

	public void setData ( String data ) {
		this.data = data;
	}
}

