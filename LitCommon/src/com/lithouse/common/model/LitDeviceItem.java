package com.lithouse.common.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement ( name = "device" )
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.LitDevice.tableName )
public class LitDeviceItem extends BaseModel {
	
	private String uid;
	private String name;
	private String type;
	private String manufacturer;
	private String scannerId;
	private String scannedDate;
	
	public LitDeviceItem ( ) { }
	
	
	@DynamoDBHashKey ( attributeName = Schema.LitDevice.uid )
	public String getUid () {
		return uid;
	}
	public void setUid ( String uid ) {
		this.uid = uid;
	}
	
	@DynamoDBAttribute ( attributeName = Schema.LitDevice.name )
	public String getName () {
		return name;
	}
	public void setName ( String name ) {
		this.name = name;
	}
	
	@DynamoDBAttribute ( attributeName = Schema.LitDevice.type )
	public String getType () {
		return type;
	}
	public void setType ( String type ) {
		this.type = type;
	}
	
	@DynamoDBAttribute ( attributeName = Schema.LitDevice.manufacturer )
	public String getManufacturer () {
		return manufacturer;
	}
	public void setManufacturer ( String manufacturer ) {
		this.manufacturer = manufacturer;
	}

	@DynamoDBAttribute ( attributeName = Schema.LitDevice.scannerId )
	public String getScannerId () {
		return scannerId;
	}
	public void setScannerId ( String scannerId ) {
		this.scannerId = scannerId;
	}

	@DynamoDBAttribute ( attributeName = Schema.LitDevice.scannedDate )
	public String getScannedDate () {
		return scannedDate;
	}
	public void setScannedDate ( String scannedDate ) {
		this.scannedDate = scannedDate;
	}
}

