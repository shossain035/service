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
import com.lithouse.common.model.RegistrationCodeItem;
import com.lithouse.common.model.RegistrationItem;
import com.lithouse.common.util.Global;


@Path ( ApiCallerConstants.Path.register )
public class RegistrationResource extends BaseResource < GenericDao > {
				
	@Inject	
	public RegistrationResource ( 
			RequestItem requestItem,
			RequestLogger requestLogger,
			Provider < GenericDao > daoProvider ) {
		super ( requestItem, requestLogger, daoProvider );
				
	}
	
	//@Authenticate
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < RegistrationItem > register ( RegistrationItem registrationItem ) throws ApiException {
		//verifyAdmin ( );
		if ( registrationItem.getCode ( ) == null 
				|| registrationItem.getCode ( ).isEmpty ( ) ) {
			throw new ApiException ( 
								ErrorCode.InvalidInput,
	 							Arrays.asList ( "code" ) );
		}
		
		registrationItem.setCode ( registrationItem.getCode ( ).toUpperCase ( ) );
		logger.info ( "registration requested for code: " + registrationItem.getCode ( ) );
		
		RegistrationCodeItem registrationCode 
			= daoProvider.get ( ).find ( RegistrationCodeItem.class, registrationItem.getCode ( ) );
		
		if ( registrationCode == null ) {
			throw new ApiException ( 
					ErrorCode.InvalidInput,
						Arrays.asList ( "code" ) );
		}
		
		//todo: make it thread safe and compare against count 
		registrationCode.setUsageCount ( registrationCode.getUsageCount ( ) + 1 );
		registrationItem.setRegisterDate ( Global.getCurrentTimestamp() );
		
		daoProvider.get ( ).batchSave ( Arrays.asList( registrationCode, registrationItem ) );
		
		return new DataBean < RegistrationItem > ( registrationItem );
	}
}
