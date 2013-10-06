package com.lithouse.api.resource;

import java.util.Arrays;

import javax.ws.rs.GET;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.response.DataBean;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.model.DeveloperItem;
import com.lithouse.api.exception.ApiException.ErrorCode;






public class DeveloperResource extends BaseResource < GenericDao > {
		
	@Inject
	public DeveloperResource ( RequestItem requestItem,
	    					   RequestLogger requestLogger,
	    					   Provider < GenericDao > daoProvider ) {
		super ( requestItem, requestLogger, daoProvider );
	}
	
	private DeveloperItem getExistingDeveloper ( ) throws ApiException {
		DeveloperItem developer = daoProvider.get ( ).find ( 
				DeveloperItem.class, requestItem.getDeveloperId ( ) );
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

//	@PUT
//	@BuildResponse
//	@Consumes ( MediaType.APPLICATION_JSON )
//	public DataBean updateDeveloper ( ) throws ApiException {
//		DeveloperItem existingDeveloper = getExistingDeveloper ( );
//		return new DataBean (  );
//	}
}
