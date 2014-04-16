package com.lithouse.api.resource;


import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.bean.ReviewListBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.model.ReviewItem;
import com.lithouse.common.util.Global;
import com.lithouse.smtp.EmailSender;


@Path ( ApiCallerConstants.Path.devicesLit 
		+ "/{" + ApiCallerConstants.PathParameters.deviceType + "}" 
		+ "/" + ApiCallerConstants.Path.reviews )
public class ReviewsResource extends BaseResource < GenericDao > {
			
	private EmailSender emailSender;
	
	@Inject	
	public ReviewsResource ( 
			RequestItem requestItem,
			RequestLogger requestLogger,
			Provider < GenericDao > daoProvider,
			EmailSender emailSender ) {
		super ( requestItem, requestLogger, daoProvider );
		
		this.emailSender = emailSender;
	}
	
//	//@Authenticate
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < ReviewItem > createSignup ( 
			@PathParam ( ApiCallerConstants.PathParameters.deviceType ) 
			String deviceType,
			ReviewItem reviewItem ) throws ApiException {
		
		reviewItem.setDeviceType ( deviceType );
		
		if ( reviewItem.getReviewerId ( ) == null 
				|| reviewItem.getReviewerId ( ).isEmpty ( ) ) {
			throw new ApiException ( 
					ErrorCode.InvalidInput, 
					Arrays.asList ( "reviewerId" ) );
		}
		
		logger.info ( "writing reviews for device: " + reviewItem.getDeviceType ( ) );
		reviewItem.setReviewDate ( Global.getCurrentTimestamp ( ) );
		
		emailSender.sendEmailAsync ( 
				"New Review for " + reviewItem.getDeviceType ( ),  
				"Reviewer Id: " + reviewItem.getReviewerId ( )
				+ "\n\nTitle: " + reviewItem.getTitle ( )
				+ "\n\nReview: " + reviewItem.getReviewText ( ) );
		
		return new DataBean < ReviewItem > ( daoProvider.get ( ).save ( reviewItem ) );
	}
	
	@GET
	@BuildResponse	
	public ReviewListBean getReviewsOfDevice ( 
								@PathParam ( ApiCallerConstants.PathParameters.deviceType ) 
								String deviceType ) throws ApiException {
		
		logger.info ( "reading reviews for device: " + deviceType );
		
		try {
			List < ReviewItem > reviews = daoProvider.get ( ).queryItems ( 
					ReviewItem.class, new ReviewItem ( deviceType ) );
			
			int totalNumberOfReviews = 0, sumOfAllRatings = 0;
			
			if ( reviews != null ) {
				for ( ReviewItem review : reviews  ) {
					totalNumberOfReviews++;
					sumOfAllRatings += review.getRating ( );
				}
			}
			
			return new ReviewListBean ( reviews, totalNumberOfReviews, sumOfAllRatings );			
		} catch ( SecurityException e ) {
			throw new ApiException ( ErrorCode.UnAuthorized, e.getMessage ( ) );
		}
	}

}
