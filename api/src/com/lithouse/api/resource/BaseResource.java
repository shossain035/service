package com.lithouse.api.resource;

import java.util.Arrays;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.util.Global;


@RequestScoped
@Produces ( MediaType.APPLICATION_JSON )
public abstract class BaseResource < DAO extends GenericDao > {
	protected final RequestItem requestItem;
	protected final RequestLogger logger;
	protected final Provider < DAO > daoProvider;

	public BaseResource ( RequestItem requestItem,
						  RequestLogger logger,
						  Provider < DAO > daoProvider ) {
		this.requestItem = requestItem;
		this.logger = logger;
		this.daoProvider = daoProvider;
	}
	
	protected Integer convertNumber ( String value, String paramName ) throws ApiException {
		if ( value == null || value.isEmpty ( ) ) {
			return null; 
		}
		
		try {
			return Integer.parseInt ( value );
		} catch ( Exception e ) {
			throw new ApiException ( ErrorCode.InvalidInput, Arrays.asList ( paramName ) );
		}
	}
	
	protected void verifyAdmin ( ) throws ApiException {
		if ( !Global.getAdminId ( ).equals ( requestItem.getDeveloperId ( ) ) ) {
			throw new ApiException ( 
					ErrorCode.UnAuthorized,
	 				Arrays.asList ( ApiCallerConstants.QueryParameters.apiKey ) );
		}
	}
}
