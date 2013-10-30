package com.lithouse.api.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.interceptor.Authenticate;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.model.ContactMessageItem;


@Path ( ApiCallerConstants.Path.contacts )
public class ContactsResource extends BaseResource < GenericDao > {
				
	@Inject	
	public ContactsResource ( 
			RequestItem requestItem,
			RequestLogger requestLogger,
			Provider < GenericDao > daoProvider ) {
		super ( requestItem, requestLogger, daoProvider );
	}
	
	@Authenticate
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < ContactMessageItem > createDeveloper ( ContactMessageItem contactMessage ) throws ApiException {
		verifyAdmin ( );
		
		logger.info ( "message: " + contactMessage.getMessageText ( ) + " saved" );
				
		return new DataBean < ContactMessageItem > ( daoProvider.get ( ).save ( contactMessage ) );
	}
}
