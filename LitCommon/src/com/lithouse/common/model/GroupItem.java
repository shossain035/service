package com.lithouse.common.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement ( name = "group" )
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.Group.tableName )
public class GroupItem extends BaseModel {
	public static class Type { 
		public static final String READ = "READ"; 
		public static final String READ_WRITE = "READ_WRITE";
		public static final List < String > checkList 
			=  new ArrayList  < String > ( Arrays.asList ( READ, READ_WRITE ) ); 
	};
	
	public static class Privacy { 
		public static final String PUBLIC = "PUBLIC";
		public static final String PRIVATE = "PRIVATE";
		public static final List < String > checkList 
			=  new ArrayList  < String > ( Arrays.asList ( PUBLIC, PRIVATE ) );
	};
	
	@XmlTransient
	private String developerId;
	private String groupId;
	private String type;
	private String groupName;
	private String groupKey;
	private String privacy;
	private String description;
	
	public GroupItem ( ) { }
	
	public GroupItem ( String developerId ) { 
		setDeveloperId ( developerId );
	}
	
	public GroupItem ( GroupItem groupItem ) {
		setPrivacy ( groupItem.getPrivacy ( ) );
		setDeveloperId ( groupItem.getDeveloperId ( ) );		
		setType ( groupItem.getType ( ) );
		setGroupName ( groupItem.getGroupName ( ) );
		setDescription ( groupItem.getDescription ( ) );
	}
	
	@DynamoDBHashKey ( attributeName = Schema.Group.developerId )
	public String getDeveloperId () {
		return developerId;
	}

	public void setDeveloperId ( String developerId ) {
		verifyTextFieldEmpty ( developerId, Schema.Group.developerId );
		this.developerId = developerId;
	}

	@DynamoDBRangeKey ( attributeName = Schema.Group.groupId )
	public String getGroupId () {
		return groupId;
	}

	public void setGroupId ( String groupId ) {
		this.groupId = groupId;
	}

	@DynamoDBAttribute ( attributeName = Schema.Group.type )
	public String getType ( ) {
		return type;
	}

	public void setType ( String type ) {
		verifyTextFieldContent ( type, Type.checkList, Schema.Group.type ); 
		this.type = type;
	}

	@DynamoDBAttribute ( attributeName = Schema.Group.groupName )
	public String getGroupName ( ) {
		return groupName;
	}

	public void setGroupName ( String groupName ) {
		verifyTextFieldEmpty ( groupName, Schema.Group.groupName );
		this.groupName = groupName;
	}

	@DynamoDBAttribute ( attributeName = Schema.Group.groupKey )
	public String getGroupKey () {
		return groupKey;
	}

	public void setGroupKey ( String groupKey ) {
		this.groupKey = groupKey;
	}

	@DynamoDBAttribute ( attributeName = Schema.Group.privacy )
	public String getPrivacy () {
		return privacy;
	}

	public void setPrivacy ( String privacy ) {
		verifyTextFieldContent ( privacy, Privacy.checkList, Schema.Group.privacy );
		//privacy can only be switch from private -> public
		if ( this.privacy != null && privacy.equalsIgnoreCase ( Privacy.PRIVATE ) ) {
			throw new IllegalArgumentException ( "'" + Schema.Group.privacy + "' can not be switched back to '" 
												 + Privacy.PRIVATE + "'" );
		}
		this.privacy = privacy;
	}

	@DynamoDBAttribute ( attributeName = Schema.Group.description )
	public String getDescription () {
		return description;
	}

	public void setDescription ( String description ) {
		this.description = ( description != null ) ? description : "";
	}
}

