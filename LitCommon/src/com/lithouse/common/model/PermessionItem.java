package com.lithouse.common.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement
@DynamoDBTable ( tableName = Schema.Permession.tableName )
public class PermessionItem extends BaseModel {
	private String groupId;
	private String appId;
	private String accessType;
	
	public PermessionItem ( ) {	}
	
	public PermessionItem ( String appId ) {	
		setAppId ( appId );
	}
	
	
	public PermessionItem ( String appId, String groupId, String accessType ) {
		setAppId ( appId );
		setGroupId ( groupId );
		setAccessType ( accessType );
	}

	@DynamoDBRangeKey ( attributeName = Schema.Permession.groupId )
	public String getGroupId () {
		return groupId;
	}

	public void setGroupId ( String groupId ) {
		verifyTextFieldEmpty ( groupId, Schema.Permession.groupId );
		this.groupId = groupId;
	}

	@DynamoDBHashKey ( attributeName = Schema.Permession.appId )
	public String getAppId () {
		return appId;
	}

	public void setAppId ( String appId ) {
		verifyTextFieldEmpty ( appId, Schema.Permession.appId );
		this.appId = appId;
	}

	@DynamoDBAttribute ( attributeName = Schema.Permession.accessType )
	public String getAccessType () {
		return accessType;
	}

	public void setAccessType ( String accessType ) {
		this.accessType = accessType;
	}

}

