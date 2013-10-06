package com.lithouse.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.Authenticate;
import com.lithouse.api.interceptor.Authenticate.Role;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.response.DataBean;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.GroupDao;
import com.lithouse.common.model.GroupItem;


@Path ( ApiCallerConstants.Path.groups )
public class GroupsResource extends BaseResource < GroupDao > {
			
	private Provider < DevicesResource > devicesProvider; 
	private Provider < RecordsResource > recordsProvider;
	
	@Inject	
	public GroupsResource ( RequestItem requestItem,
				    	    RequestLogger requestLogger,
				    	    Provider < GroupDao > daoProvider,
				    	    Provider < DevicesResource > devicesProvider,
				    	    Provider < RecordsResource > recordsProvider) {
		super ( requestItem, requestLogger, daoProvider );
		this.devicesProvider = devicesProvider;
		this.recordsProvider = recordsProvider;
	}
	
	@Authenticate
	@GET 
	@BuildResponse
	public DataBean < GroupItem > getAllGroupsByDeveloperId ( ) {
		logger.info ( "fetching groups by developerId: " + requestItem.getDeveloperId ( ) );
		return new DataBean < GroupItem > ( 
						daoProvider.get ( ).getAllGroups ( requestItem.getDeveloperId ( ) ) );
	}  
	
	@Authenticate
	@Path ( "/{" + ApiCallerConstants.PathParameters.groupId + "}/" + ApiCallerConstants.Path.devices )	
	public DevicesResource getDevicesResource ( ) throws ApiException {
	    return devicesProvider.get ( );
	}
	
	@Authenticate ( Role.APP )
	@Path ( "/{" + ApiCallerConstants.PathParameters.groupId + "}/" + ApiCallerConstants.Path.records )	
	public RecordsResource getRecordsResource ( ) throws ApiException {
	    return recordsProvider.get ( );
	}
	
	@Authenticate
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public GroupItem createGroup ( GroupItem groupItem ) throws ApiException {				
		groupItem.setDeveloperId ( requestItem.getDeveloperId ( ) );
		logger.info ( "creating group " + groupItem.getGroupName ( ) 
				+ " for developerId: " + requestItem.getDeveloperId ( ) );
		
		try { 
			return daoProvider.get ( ).createGroup ( groupItem );
		} catch ( IllegalArgumentException e ) {
			throw new ApiException ( ErrorCode.InvalidInput, e. getMessage ( ) );
		}
	}
	
}
