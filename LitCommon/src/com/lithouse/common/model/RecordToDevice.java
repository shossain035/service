package com.lithouse.common.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement ( name = "record" )
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.RecordToDevice.tableName )
public class RecordToDevice extends BaseModel {
	
	private String deviceId;
	private String timeStamp;
	@XmlTransient
	private String groupId;
	private String channel;
	private String data;
	@XmlTransient
	private String appId;
	
	public RecordToDevice ( ) {	}

	@DynamoDBHashKey ( attributeName = Schema.RecordToDevice.deviceId )
	public String getDeviceId () {
		return deviceId;
	}

	public void setDeviceId ( String deviceId ) {
		this.deviceId = deviceId;
	}

	@DynamoDBRangeKey ( attributeName = Schema.RecordToDevice.timeStamp )
	public String getTimeStamp () {
		return timeStamp;
	}

	public void setTimeStamp ( String timeStamp ) {
		this.timeStamp = timeStamp;
	}

	@DynamoDBAttribute ( attributeName = Schema.RecordToDevice.groupId )
	public String getGroupId () {
		return groupId;
	}

	public void setGroupId ( String groupId ) {
		this.groupId = groupId;
	}

	@DynamoDBAttribute ( attributeName = Schema.RecordToDevice.channel )
	public String getChannel () {
		return channel;
	}

	public void setChannel ( String channel ) {
		this.channel = channel;
	}

	@DynamoDBAttribute ( attributeName = Schema.RecordToDevice.data )
	public String getData () {
		return data;
	}

	public void setData ( String data ) {
		this.data = data;
	}

	@DynamoDBAttribute ( attributeName = Schema.RecordToDevice.appId )
	public String getAppId () {
		return appId;
	}

	public void setAppId ( String appId ) {
		this.appId = appId;
	}
	
}

