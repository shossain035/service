package com.lithouse.common.dao.impl;


import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.lithouse.common.dao.DBFilter;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.util.Global;

public class GenericDaoImpl implements GenericDao {

	protected DynamoDBMapper mapper;
	
	public GenericDaoImpl ( ) {
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient ( Global.getAwsCredentials ( ) );
		String ddbEndpoint = Global.getConfig ( ).getString( "ddb-" + Global.getAwsRegion ( ) );		
		ddbClient.setEndpoint ( ddbEndpoint );
		
		mapper = new DynamoDBMapper ( ddbClient );
	}
	
	@Override
	public < T > T save ( T t ) {
		mapper.save ( t );
		return t;
	}

	@Override
	public < T, H > T find ( Class <T> clazz, H hashKey ) {
		if ( hashKey == null ) {
			throw new IllegalArgumentException ( "HashKey cannot be null" );
			
		}
		return ( T ) mapper.load ( clazz, hashKey );
	}

	@Override
	public < T, H, R > T find ( Class <T> clazz, H hashKey, R rangeKey ) {
		if ( hashKey == null || rangeKey == null  ) {
			throw new IllegalArgumentException ( "HashKey or RangeKey cannot be null" );
			
		}
		
		return ( T ) mapper.load ( clazz, hashKey, rangeKey );
	}

	@Override
	public < T > void remove ( T t ) {
		mapper.delete ( t );
	}

	@Override
	public < T > List < T > queryItems ( Class < T > clazz, T hashKeyObject, DBFilter < String > rangeKeyFilter ) {
		if ( hashKeyObject == null ) {
			throw new IllegalArgumentException ( "HashKey or RangeKeyFilter cannot be null" );
		}
		
		return mapper.query ( clazz, getQueryExpression ( hashKeyObject, rangeKeyFilter ) );
	}
	
	private < T > DynamoDBQueryExpression < T > getQueryExpression  ( 
														T hashKeyObject, 
														DBFilter < String > rangeKeyFilter ) {
		List < AttributeValue > attributeValues = new ArrayList < AttributeValue > ();
		attributeValues.add ( new AttributeValue ( )
									.withS ( rangeKeyFilter.getAttributeValue ( ) ) );
		
		if ( ComparisonOperator.BETWEEN == rangeKeyFilter.getOperator ( ) ) {
			attributeValues.add ( new AttributeValue ( rangeKeyFilter.getAttributeValueEnd ( ) ));
		}
		
		Condition rangeKeyCondition = new Condition ( ).withComparisonOperator ( rangeKeyFilter.getOperator ( ) )
													   .withAttributeValueList ( attributeValues );	
				
		return new DynamoDBQueryExpression < T > ( )
					.withHashKeyValues ( hashKeyObject )
					.withRangeKeyCondition ( rangeKeyFilter.getAttribute ( ), rangeKeyCondition );
	}

	@Override
	public < T > List < T > queryItems ( Class < T > clazz, T hashKeyObject ) {
		if (hashKeyObject == null ) {
			throw new IllegalArgumentException ( "HashKey cannot be null" );
		}
		
		return mapper.query ( clazz, new DynamoDBQueryExpression < T > ( )
											.withHashKeyValues ( hashKeyObject ) );
	}
}
