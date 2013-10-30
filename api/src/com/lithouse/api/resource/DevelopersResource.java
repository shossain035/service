package com.lithouse.api.resource;

import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.Authenticate;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.model.ApiKeyItem;
import com.lithouse.common.model.DeveloperItem;
import com.lithouse.common.util.Global;


@Path ( ApiCallerConstants.Path.developers )
public class DevelopersResource extends BaseResource < GenericDao > {
			
	private Provider < DeveloperResource > developerProvider; 
	
	@Inject	
	public DevelopersResource ( RequestItem requestItem,
						    	RequestLogger requestLogger,
						    	Provider < GenericDao > daoProvider,
						    	Provider < DeveloperResource > developerProvider ) {
		super ( requestItem, requestLogger, daoProvider );
		this.developerProvider = developerProvider;		
	}
	
	@Authenticate
	@Path ( "/{" + ApiCallerConstants.PathParameters.developerId + "}" )	
	public DeveloperResource getDeveloperResource ( 
								@PathParam ( ApiCallerConstants.PathParameters.developerId ) 
								String developerId ) throws ApiException {
		
		verifyAdmin ( );
		
		if ( developerId == null || developerId.isEmpty ( ) ) {
			throw new ApiException ( ErrorCode.InvalidInput,
				 		Arrays.asList ( ApiCallerConstants.PathParameters.developerId ) );
		}
		
		logger.info ( "developerId: " + developerId );
		requestItem.setDeveloperId ( developerId );
		
	    return developerProvider.get ( );
	}
	
	private void verifyNewDeveloper ( DeveloperItem developer ) throws ApiException {
		if ( developer == null || developer.getDeveloperId ( ) == null 
				|| developer.getDeveloperId ( ).isEmpty ( ) ) {
			throw new ApiException ( 
						ErrorCode.InvalidInput,
						ApiCallerConstants.PathParameters.developerId,
						"cannot be blank" );
		}
		
		if ( daoProvider.get ( ).find ( DeveloperItem.class, developer.getDeveloperId ( ) ) != null ) {
			throw new ApiException ( 
						ErrorCode.InvalidInput,
						ApiCallerConstants.PathParameters.developerId,
						"is already in use" );
		}
		
		//verify other attributes.
	}
	
	@Authenticate
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < DeveloperItem > createDeveloper ( DeveloperItem developer ) throws ApiException {
		verifyAdmin ( );
		verifyNewDeveloper ( developer );
	
		logger.info ( "developerId: " + developer.getDeveloperId ( ) + " saved" );
		
		ApiKeyItem apiKeyItem = new ApiKeyItem ( developer.getDeveloperId ( ) );
		daoProvider.get ( ).save ( apiKeyItem );
		
		developer.setApiKey ( apiKeyItem.getApiKey ( ) );
		developer.setDeviceLimit ( Global.getConfig ( ).getInt ( "lithouse.developer.device.limit" ) );
		
		return new DataBean < DeveloperItem > ( daoProvider.get ( ).save ( developer ) );
	}
}
