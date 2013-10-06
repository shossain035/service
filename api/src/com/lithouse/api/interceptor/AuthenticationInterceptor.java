package com.lithouse.api.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.aopalliance.intercept.MethodInvocation;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.Authenticate.Role;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.model.ApiKeyItem;
import com.lithouse.common.model.AppKeyItem;
import com.lithouse.common.model.BaseModel;
import com.lithouse.common.model.GroupKeyItem;

public class AuthenticationInterceptor extends BaseInterceptor {
	private final Provider < HttpServletRequest > servletRequestProvider;
	private final Provider < GenericDao > daoProvider;

	@Inject
	public AuthenticationInterceptor ( Provider < RequestItem > requestItemProvider,
	 								   Provider < RequestLogger > loggerProvider,
	 								   Provider < HttpServletRequest > servletRequestProvider,
	 								   Provider < GenericDao > daoProvider ) {	
		super ( requestItemProvider, loggerProvider);
		
		this.servletRequestProvider = servletRequestProvider;
		this.daoProvider = daoProvider;
	}
	
	@Override
	public Object invoke ( MethodInvocation invocation, 
						   String methodName,
						   RequestItem requestItem, 
						   RequestLogger logger ) throws Throwable {
		
		Role role = invocation.getMethod ( ).getAnnotation ( Authenticate.class ).value ( );		
		logger.info ( "@Authenticate::" + methodName + " for " + role ); 
		
		if ( role == Role.DEVELOPER ) {
			authenticateDeveloper ( requestItem, logger );
		} else if ( role == Role.APP ) {
			authenticateApp ( requestItem, logger );
		} else if ( role == Role.GROUP ) {
			authenticateGroup ( requestItem, logger );
		}

		return invocation.proceed();
	}
	
	private void authenticateApp ( 
			RequestItem requestItem, RequestLogger logger ) throws ApiException {
		
		AppKeyItem hashKeyItem = new AppKeyItem ( ); 
		hashKeyItem.setAppKey ( getHaskKey ( servletRequestProvider.get ( ), 
											 ApiCallerConstants.QueryParameters.appKey ) );
		
		AppKeyItem dbKey = getKeyFromDB ( 
				ApiCallerConstants.QueryParameters.appKey, AppKeyItem.class, hashKeyItem );
		requestItem.setAppId ( dbKey.getAppId ( ) );
		requestItem.setDeveloperId ( dbKey.getDeveloperId ( ) );
		
		logger.info ( "developerId: " + requestItem.getDeveloperId ( ) 
						+ " appId: " + requestItem.getAppId ( ) );
	}
	
	private void authenticateGroup ( 
			RequestItem requestItem, RequestLogger logger ) throws ApiException {
		
		GroupKeyItem hashKeyItem = new GroupKeyItem ( ); 
		hashKeyItem.setGroupKey ( getHaskKey ( servletRequestProvider.get ( ), 
											   ApiCallerConstants.QueryParameters.groupKey ) );
		
		GroupKeyItem dbKey = getKeyFromDB ( 
				ApiCallerConstants.QueryParameters.groupKey, GroupKeyItem.class, hashKeyItem );
		requestItem.setGroupId ( dbKey.getGroupId ( ) );
		requestItem.setDeveloperId ( dbKey.getDeveloperId ( ) );
		
		logger.info ( "developerId: " + requestItem.getDeveloperId ( ) 
						+ " groupId: " + requestItem.getGroupId ( ) );
	}
	
	private void authenticateDeveloper ( 
			RequestItem requestItem, RequestLogger logger ) throws ApiException {
		
		String apiKey = getHaskKey ( servletRequestProvider.get ( ), 
								  	 ApiCallerConstants.QueryParameters.apiKey );
		
		ApiKeyItem keyItem = daoProvider.get ( ).find ( ApiKeyItem.class, apiKey );
		if ( null == keyItem ) {
			throw new ApiException ( ErrorCode.UnAuthenticated,
					 Arrays.asList( ApiCallerConstants.QueryParameters.apiKey ) );
		}
		
		requestItem.setDeveloperId ( keyItem.getDeveloperId ( ) );
		logger.info ( "callerId: " + requestItem.getDeveloperId ( ) );
	}
	
	private String getHaskKey ( HttpServletRequest request, 
								String qeryParamName ) throws ApiException {
		
		String key = request.getParameter ( qeryParamName );
		
		if ( key == null || key.isEmpty ( ) ) {
			throw new ApiException ( ErrorCode.UnAuthenticated,
									 Arrays.asList( qeryParamName ) );					 
		}
		
		return key;
	}
	
	private < T, C extends BaseModel > C getKeyFromDB ( 
								String qeryParamName, 
								Class < C > clazz,
								C hashKeyItem ) throws ApiException {
		
		
		List < C > keyItems = daoProvider.get ( ).queryItems ( clazz, hashKeyItem );
		//using query instead of find to avoid range key issues with app and group key
		if ( keyItems.size ( ) != 1 ) {
			throw new ApiException ( ErrorCode.UnAuthenticated,
					 				 Arrays.asList( qeryParamName ) );
		}
		
		return keyItems.get ( 0 );
	}

}
