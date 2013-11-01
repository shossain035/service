package com.lithouse.common.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement ( name = "permission" )
@DynamoDBTable ( tableName = Schema.Permission.tableName )
public class PermissionItem extends BaseModel {
	private String groupId;
	private String appId;
	private String accessType;
	
	public PermissionItem ( ) {	}
	
	public PermissionItem ( String appId ) {	
		setAppId ( appId );
	}
	
	
	public PermissionItem ( String appId, String groupId, String accessType ) {
		setAppId ( appId );
		setGroupId ( groupId );
		setAccessType ( accessType );
	}

	@DynamoDBRangeKey ( attributeName = Schema.Permission.groupId )
	public String getGroupId () {
		return groupId;
	}

	public void setGroupId ( String groupId ) {
		verifyTextFieldEmpty ( groupId, Schema.Permission.groupId );
		this.groupId = groupId;
	}

	@DynamoDBHashKey ( attributeName = Schema.Permission.appId )
	public String getAppId () {
		return appId;
	}

	public void setAppId ( String appId ) {
		verifyTextFieldEmpty ( appId, Schema.Permission.appId );
		this.appId = appId;
	}

	@DynamoDBAttribute ( attributeName = Schema.Permission.accessType )
	public String getAccessType () {
		return accessType;
	}

	public void setAccessType ( String accessType ) {
		this.accessType = accessType;
	}

}

