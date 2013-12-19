package com.lithouse.api.resource;

import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.DeveloperDao;
import com.lithouse.common.model.DeveloperItem;
import com.lithouse.api.exception.ApiException.ErrorCode;






public class DeveloperResource extends BaseResource < DeveloperDao > {
		
	@Inject
	public DeveloperResource ( RequestItem requestItem,
	    					   RequestLogger requestLogger,
	    					   Provider < DeveloperDao > daoProvider ) {
		super ( requestItem, requestLogger, daoProvider );
	}
	
	private DeveloperItem getExistingDeveloper ( ) throws ApiException {
		DeveloperItem developer = daoProvider.get ( )
					.findWithIFTTTVerification ( requestItem.getDeveloperId ( ) );
		
		if ( developer == null ) {
			throw new ApiException ( 
						ErrorCode.InvalidInput,
						Arrays.asList ( ApiCallerConstants.PathParameters.developerId ) );
		}
		
		return developer;
	}
	
	@GET
	@BuildResponse	
	public DataBean < DeveloperItem > getDeveloper ( ) throws ApiException {
		return new DataBean < DeveloperItem > ( getExistingDeveloper ( ) );
	}

	@PUT
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < DeveloperItem > updateDeveloper ( DeveloperItem developer ) throws ApiException {
		if ( developer.getDeveloperId ( ) == null || developer.getDeveloperId ( ).isEmpty ( ) ) {
			throw new ApiException ( 
					ErrorCode.InvalidInput,
					Arrays.asList ( ApiCallerConstants.PathParameters.developerId ) );
		}
		
		return new DataBean < DeveloperItem > ( daoProvider.get ( ).updateDeveloper ( developer ) );
	}
}
