package com.lithouse.common.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.RecordToDevice.tableName )
public class RecordToDevice extends Record {
	
	@XmlTransient
	private String appId;
	
	public RecordToDevice ( ) {	}

	@DynamoDBAttribute ( attributeName = Schema.RecordToDevice.appId )
	public String getAppId () {
		return appId;
	}

	public void setAppId ( String appId ) {
		this.appId = appId;
	}
	
}

