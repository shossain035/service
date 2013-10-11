package com.lithouse.common.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.LatestRecordFromDevice.tableName )
public class LatestRecordFromDeviceItem extends BaseModel  {
	
	private String deviceId;
	private String timeStamp;
	@XmlTransient
	private String groupId;
	@XmlTransient
	private String rangeKey;
	private String channel;
	private String data;
	
	public LatestRecordFromDeviceItem ( ) {	}
	
	public LatestRecordFromDeviceItem ( String groupId ) {	
		this.groupId = groupId;
	}
	
	public LatestRecordFromDeviceItem ( String groupId, String rangeKey ) {	
		this.groupId = groupId;
		this.rangeKey = rangeKey;
	}

	@DynamoDBAttribute ( attributeName = Schema.LatestRecordFromDevice.deviceId )
	public String getDeviceId () {
		return deviceId;
	}

	public void setDeviceId ( String deviceId ) {
		this.deviceId = deviceId;
	}

	@DynamoDBAttribute ( attributeName = Schema.LatestRecordFromDevice.timeStamp )
	public String getTimeStamp () {
		return timeStamp;
	}

	public void setTimeStamp ( String timeStamp ) {
		this.timeStamp = timeStamp;
	}

	@DynamoDBHashKey ( attributeName = Schema.LatestRecordFromDevice.groupId )
	public String getGroupId () {
		return groupId;
	}

	public void setGroupId ( String groupId ) {
		this.groupId = groupId;
	}
	
	@DynamoDBIndexRangeKey ( attributeName = Schema.LatestRecordFromDevice.channel, 
							 localSecondaryIndexName = Schema.LatestRecordFromDevice.channel + "-index" )
	public String getChannel () {
		return channel;
	}

	public void setChannel ( String channel ) {
		this.channel = channel;
	}

	@DynamoDBAttribute ( attributeName = Schema.LatestRecordFromDevice.data )
	public String getData () {
		return data;
	}

	public void setData ( String data ) {
		this.data = data;
	}

	@DynamoDBRangeKey ( attributeName = Schema.LatestRecordFromDevice.rangeKey )
	public String getRangeKey () {
		return rangeKey;
	}

	public void setRangeKey ( String rangeKey ) {
		this.rangeKey = rangeKey;
	}
}

