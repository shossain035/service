package com.lithouse.api.resource;


import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.model.SignupItem;
import com.lithouse.smtp.EmailSender;


@Path ( ApiCallerConstants.Path.signups )
public class SignupsResource extends BaseResource < GenericDao > {
			
	//private EmailSender emailSender;
	
	@Inject	
	public SignupsResource ( 
			RequestItem requestItem,
			RequestLogger requestLogger,
			Provider < GenericDao > daoProvider,
			EmailSender emailSender ) {
		super ( requestItem, requestLogger, daoProvider );
		
		//this.emailSender = emailSender;
	}
	
	//@Authenticate
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < SignupItem > createSignup ( SignupItem signupItem ) throws ApiException {
		//verifyAdmin ( );
		if ( signupItem.getEmailAddress ( ) == null || signupItem.getEmailAddress ( ).isEmpty ( ) ) {
			throw new ApiException ( 
					ErrorCode.InvalidInput,
	 				Arrays.asList ( "emailAddress" ) );
		}
		
		logger.info ( "message: " + signupItem.getEmailAddress ( ) + " saved" );
		
//		emailSender.sendEmailAsync ( "New Contact", "From: " + contactMessage.getEmailAddress ( ) 
//				+ "\n\nUser Id: " + contactMessage.getDeveloperId ( )
//				+ "\n\nMessage: " + contactMessage.getMessageText ( ) );
//		
		return new DataBean < SignupItem > ( daoProvider.get ( ).save ( signupItem ) );
	}
}
