package com.lithouse.common.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.Record.tableName )
public class LatestRecord extends BaseModel  {
	
	private String deviceId;
	private String timeStamp;
	@XmlTransient
	private String groupId;
	private String channel;
	private String data;
	
	public LatestRecord ( ) {	}

	@DynamoDBHashKey ( attributeName = Schema.Record.deviceId )
	public String getDeviceId () {
		return deviceId;
	}

	public void setDeviceId ( String deviceId ) {
		this.deviceId = deviceId;
	}

	@DynamoDBRangeKey ( attributeName = Schema.Record.timeStamp )
	public String getTimeStamp () {
		return timeStamp;
	}

	public void setTimeStamp ( String timeStamp ) {
		this.timeStamp = timeStamp;
	}

	@DynamoDBAttribute ( attributeName = Schema.Record.groupId )
	public String getGroupId () {
		return groupId;
	}

	public void setGroupId ( String groupId ) {
		this.groupId = groupId;
	}

	@DynamoDBAttribute ( attributeName = Schema.Record.channel )
	public String getChannel () {
		return channel;
	}

	public void setDataHead ( String channel ) {
		this.channel = channel;
	}

	@DynamoDBAttribute ( attributeName = Schema.Record.data )
	public String getData () {
		return data;
	}

	public void setData ( String data ) {
		this.data = data;
	}
}

