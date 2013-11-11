package com.lithouse.api.resource;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DeviceListBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.DeviceDao;


public class DevicesResource extends BaseResource < DeviceDao > {
			
	@Inject	
	public DevicesResource ( RequestItem requestItem,
					    	 RequestLogger requestLogger,
					    	 Provider < DeviceDao > daoProvider ) {
		super ( requestItem, requestLogger, daoProvider );
	}
	
	@GET
	@BuildResponse
	public DeviceListBean getDevices ( 
								@PathParam ( ApiCallerConstants.PathParameters.groupId ) 
								String groupId ) throws ApiException {
		
		logger.info ( "fetching devices from group: " 
				+ groupId + " for developerId: " + requestItem.getDeveloperId ( ) );
		
		try {
			return new DeviceListBean ( 
	    				daoProvider.get ( ).getAllDevices ( requestItem.getDeveloperId ( ), groupId ) );
		} catch ( IllegalArgumentException e ) {
			throw new ApiException ( ErrorCode.InvalidInput, e.getMessage ( ) );
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.UnAuthenticated, 
					 "You do not have access to all the devices in this group" );
		}
	}
	
	@POST
	@BuildResponse
	public DeviceListBean createDevices ( 
								@PathParam ( ApiCallerConstants.PathParameters.groupId ) 
								String groupId,
								@QueryParam ( ApiCallerConstants.QueryParameters.count )
								String count ) throws ApiException {
		int requestedDeviceCount = getRequestedDeviceCount ( count );
		
		logger.info ( "creating " + requestedDeviceCount 
						+ " devices for developerId: " + requestItem.getDeveloperId ( ) );
		
		try {
			return new DeviceListBean ( 
	    				daoProvider.get ( ).createDevices ( 
	    						requestItem.getDeveloperId ( ), groupId, requestedDeviceCount ) );
		} catch ( IllegalArgumentException e ) {
			throw new ApiException ( ErrorCode.InvalidInput, e.getMessage ( ) );
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.InvalidInput, 
									 Arrays.asList ( ApiCallerConstants.PathParameters.groupId ) );
		}
	}
}
