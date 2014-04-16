package com.lithouse.common.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@XmlRootElement ( name = "review" )
@XmlAccessorType ( XmlAccessType.FIELD )
@DynamoDBTable ( tableName = Schema.Review.tableName )

public class ReviewItem extends BaseModel {
	private String deviceType;
	private String reviewerId;
	private int rating;
	private String title;
	private String reviewText;
	private String reviewDate;

	public ReviewItem ( ) { }
	
	public ReviewItem ( String deviceType ) { 
		setDeviceType ( deviceType );
	}
	
	@DynamoDBHashKey ( attributeName = Schema.Review.deviceType )
	public String getDeviceType () {
		return deviceType;
	}
	public void setDeviceType ( String deviceType ) {
		verifyTextFieldEmpty ( deviceType, Schema.Review.deviceType );
		this.deviceType = deviceType;
	}
	
	@DynamoDBRangeKey ( attributeName = Schema.Review.reviewerId )
	public String getReviewerId () {
		return reviewerId;
	}
	public void setReviewerId ( String reviewerId ) {
		this.reviewerId = reviewerId;
	}
	
	@DynamoDBAttribute ( attributeName = Schema.Review.rating )
	public int getRating () {
		return rating;
	}
	public void setRating ( int rating ) {
		this.rating = rating;
	}
	
	@DynamoDBAttribute ( attributeName = Schema.Review.title )
	public String getTitle () {
		return title;
	}
	public void setTitle ( String title ) {
		this.title = title;
	}

	@DynamoDBAttribute ( attributeName = Schema.Review.reviewText )
	public String getReviewText () {
		return reviewText;
	}
	public void setReviewText ( String reviewText ) {
		this.reviewText = reviewText;
	}

	@DynamoDBAttribute ( attributeName = Schema.Review.reviewDate )
	public String getReviewDate () {
		return reviewDate;
	}
	public void setReviewDate ( String reviewDate ) {
		this.reviewDate = reviewDate;
	}
}

