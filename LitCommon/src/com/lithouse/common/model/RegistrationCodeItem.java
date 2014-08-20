package com.lithouse.common.model;



import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable ( tableName = Schema.RegistrationCode.tableName )
public class RegistrationCodeItem extends BaseModel  {
	private String code;
	private Integer usageCount;	
	
	public RegistrationCodeItem ( ) { }
	
	@DynamoDBHashKey ( attributeName = Schema.RegistrationCode.code )
	public String getCode () {
		return code;
	}

	public void setCode ( String code ) {
		this.code = code;
	}

	@DynamoDBAttribute ( attributeName = Schema.RegistrationCode.usageCount )
	public Integer getUsageCount ( ) {
		return usageCount != null ? usageCount : 0;
	}

	public void setUsageCount ( Integer usageCount ) {
		this.usageCount = usageCount;
	}

}

