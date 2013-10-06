package com.lithouse.common.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@XmlRootElement ( name = "developer" )
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.Developer.tableName )
public class DeveloperItem extends BaseModel {

	@XmlTransient
    private Integer version;
    private String developerId;
	private Integer deviceCount;
	private Integer writeDeviceCount;
	private Integer appCount;
	private String firstName;
	private String lastName;
	private String apiKey;
	private String emailAddress;
	private Integer deviceLimit;
	
	public DeveloperItem ( ) { }

	@DynamoDBHashKey ( attributeName = Schema.Developer.developerId )
	public String getDeveloperId () {
		return developerId;
	}

	public void setDeveloperId ( String developerId ) {
		this.developerId = developerId;
	}

	@DynamoDBAttribute ( attributeName = Schema.Developer.deviceCount )
	public Integer getDeviceCount () {
		return deviceCount != null ? deviceCount : 0;
	}

	public void setDeviceCount ( Integer deviceCount ) {
		this.deviceCount = deviceCount;
	}

	@DynamoDBAttribute ( attributeName = Schema.Developer.writeDeviceCount )
	public Integer getWriteDeviceCount () {
		return writeDeviceCount != null ? writeDeviceCount : 0;
	}

	public void setWriteDeviceCount ( Integer writeDeviceCount ) {
		this.writeDeviceCount = writeDeviceCount;
	}

	@DynamoDBAttribute ( attributeName = Schema.Developer.appCount )
	public Integer getAppCount ( ) {
		return appCount != null ? appCount : 0;
	}

	public void setAppCount ( Integer appCount ) {
		this.appCount = appCount;
	}

	@DynamoDBAttribute ( attributeName = Schema.Developer.firstName )
	public String getFirstName ( ) {
		return firstName;
	}

	public void setFirstName ( String firstName ) {
		this.firstName = firstName;
	}

	@DynamoDBAttribute ( attributeName = Schema.Developer.lastName )
	public String getLastName ( ) {
		return lastName;
	}

	public void setLastName ( String lastName ) {
		this.lastName = lastName;
	}

	@DynamoDBAttribute ( attributeName = Schema.Developer.apiKey )
	public String getApiKey ( ) {
		return apiKey;
	}

	public void setApiKey ( String apiKey ) {
		this.apiKey = apiKey;
	}

	@DynamoDBAttribute ( attributeName = Schema.Developer.emailAddress )
	public String getEmailAddress ( ) {
		return emailAddress;
	}

	public void setEmailAddress ( String emailAddress ) {
		this.emailAddress = emailAddress;
	}

	@DynamoDBVersionAttribute
    public Integer getVersion() { return version; }    
    public void setVersion(Integer version) { this.version = version; }

    @DynamoDBAttribute ( attributeName = Schema.Developer.deviceLimit )
	public Integer getDeviceLimit () {
		return deviceLimit;
	}

	public void setDeviceLimit ( Integer deviceLimit ) {
		this.deviceLimit = deviceLimit;
	}	
}

