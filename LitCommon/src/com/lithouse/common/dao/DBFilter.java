package com.lithouse.common.dao;

import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;


public class DBFilter < V > {
	private final String attribute;
	private final V attributeValue;
	private final V attributeValueEnd;
	private final ComparisonOperator operator;
	
	public DBFilter ( String attribute, 
					  V attributeValue,
					  ComparisonOperator operator ) {
		this ( attribute, attributeValue, null, operator );
	}
	
	public DBFilter ( String attribute, 
					  V attributeValue,
					  V attributeValueEnd,
					  ComparisonOperator operator ) {
		
		this.attribute = attribute;
		this.attributeValue = attributeValue;
		this.operator = operator;
		this.attributeValueEnd = attributeValueEnd;
	}
	
	public String getAttribute() {
		return attribute;
	}
	public V getAttributeValue() {
		return attributeValue;
	}
	public ComparisonOperator getOperator() {
		return operator;
	}
	public V getAttributeValueEnd() {
		return attributeValueEnd;
	}
}