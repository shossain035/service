package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.ReviewItem;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class ReviewListBean extends BaseBean {
	private int totalNumberOfReviews;
	private int sumOfAllRatings;
	
	//TODO: Remove hard coding from these names 
	@XmlElement ( name = "reviews" )
	private List < ReviewItem > resultList;
	
	public ReviewListBean ( ) {}
	
	public ReviewListBean ( List < ReviewItem > list, 
							int totalNumberOfReviews, 
							int sumOfAllRatings ) { 
		
		this.resultList = list;
		this.totalNumberOfReviews = totalNumberOfReviews;
		this.sumOfAllRatings = sumOfAllRatings;
	}

	public List < ReviewItem > getList ( ) {
		return resultList;
	}
	
	public int getTotalNumberOfReviews ( ) {
		return totalNumberOfReviews;
	}
	
	public int getSumOfAllRatings ( ) {
		return sumOfAllRatings;
	}
}
