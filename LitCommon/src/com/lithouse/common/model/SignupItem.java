package com.lithouse.common.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement ( name = "signup" )
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.Signup.tableName )
public class SignupItem extends BaseModel  {
	private String signupId;
	private String emailAddress;
	private boolean didRequestNewsletter;	
	
	public SignupItem ( ) { }
	
	@DynamoDBHashKey ( attributeName = Schema.Signup.signupId )
	@DynamoDBAutoGeneratedKey
	public String getSignupId () {
		return signupId;
	}

	public void setSignupId ( String signupId ) {
		this.signupId = signupId;
	}

	@DynamoDBAttribute ( attributeName = Schema.Signup.emailAddress )
	public String getEmailAddress () {
		return emailAddress;
	}

	public void setEmailAddress ( String emailAddress ) {
		this.emailAddress = emailAddress;
	}

	@DynamoDBAttribute ( attributeName = Schema.Signup.didRequestNewsletter )
	public boolean getDidRequestNewsletter ( ) {
		return didRequestNewsletter;
	}

	public void setDidRequestNewsletter ( boolean didRequestNewsletter ) {
		this.didRequestNewsletter = didRequestNewsletter;
	}

}

