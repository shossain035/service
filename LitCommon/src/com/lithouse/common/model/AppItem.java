package com.lithouse.common.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement ( name = "app" )
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.App.tableName )
public class AppItem extends BaseModel  {
	@XmlTransient
	private String developerId;
	private String appId;
	private String appName;
	private String appKey;
	private String description;
	
	public AppItem ( ) { }
	
	public AppItem ( String developerId ) { 
		setDeveloperId ( developerId );
	}
	
	public AppItem ( AppItem appItem ) {
		setDeveloperId ( appItem.getDeveloperId ( ) );		
		setAppName ( appItem.getAppName ( ) );
		setDescription ( appItem.getDescription ( ) );
	}
	
	@DynamoDBHashKey ( attributeName = Schema.App.developerId )
	public String getDeveloperId () {
		return developerId;
	}

	public void setDeveloperId ( String developerId ) {
		verifyTextFieldEmpty ( developerId, Schema.App.developerId );
		this.developerId = developerId;
	}

	@DynamoDBRangeKey ( attributeName = Schema.App.appId )
	public String getAppId () {
		return appId;
	}

	public void setAppId ( String appId ) {
		this.appId = appId;
	}

	@DynamoDBAttribute ( attributeName = Schema.App.appName )
	public String getAppName ( ) {
		return appName;
	}

	public void setAppName ( String appName ) {
		verifyTextFieldEmpty ( appName, Schema.App.appName );
		this.appName = appName;
	}

	@DynamoDBAttribute ( attributeName = Schema.App.appKey )
	public String getAppKey () {
		return appKey;
	}

	public void setAppKey ( String appKey ) {
		this.appKey = appKey;
	}

	@DynamoDBAttribute ( attributeName = Schema.App.description )
	public String getDescription () {
		return description;
	}

	public void setDescription ( String description ) {
		this.description = ( description != null ) ? description : "";
	}
}

