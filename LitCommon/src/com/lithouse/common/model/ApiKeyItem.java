package com.lithouse.common.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


@DynamoDBTable ( tableName = Schema.ApiKey.tableName )
public class ApiKeyItem extends BaseModel {
	private String apiKey;
	private String developerId;
	
	public ApiKeyItem ( ) {	}
	
	public ApiKeyItem ( String developerId ) {
		setDeveloperId ( developerId ); 
	}
	
	@DynamoDBHashKey ( attributeName = Schema.ApiKey.apiKey )
	@DynamoDBAutoGeneratedKey
	public String getApiKey ( ) {
		return apiKey;
	}
	
	public void setApiKey ( String apiKey ) {
		this.apiKey = apiKey;
	}

	@DynamoDBAttribute ( attributeName = Schema.ApiKey.developerId )
	public String getDeveloperId ( ) {
		verifyTextFieldEmpty ( developerId, Schema.ApiKey.developerId );
		return developerId;
	}

	public void setDeveloperId ( String developerId ) {
		this.developerId = developerId;
	}

}

