package com.lithouse.api.resource;



import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.bean.PermessionListBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.AppDao;
import com.lithouse.common.model.AppItem;
import com.lithouse.common.model.PermessionItem;

public class AppResource extends BaseResource < AppDao > {
	
	@Inject	
	public AppResource ( RequestItem requestItem,
					   	 RequestLogger requestLogger,
					   	 Provider < AppDao > daoProvider ) {
		super ( requestItem, requestLogger, daoProvider );
	}
	
	@GET
	@BuildResponse
	@Path ( "/{" + ApiCallerConstants.PathParameters.appId + "}" )
	public DataBean < AppItem > getApp ( 
			@PathParam ( ApiCallerConstants.PathParameters.appId )
			String appId ) throws ApiException {
		
		logger.info ( "fetching appId: " + appId 
						+ "developerId: " + requestItem.getDeveloperId ( ) );		
		return new DataBean < AppItem > ( 
				daoProvider.get ( ).find ( AppItem.class, requestItem.getDeveloperId ( ), appId ) );
	}
	
	@PUT
	@BuildResponse
	@Path ( "/{" + ApiCallerConstants.PathParameters.appId + "}/" + ApiCallerConstants.Path.permessions )
	public PermessionListBean putPermessions ( 
					@PathParam ( ApiCallerConstants.PathParameters.appId ) 
					String appId,
					@QueryParam ( ApiCallerConstants.QueryParameters.groupId )
					List < String > groupIds ) throws ApiException {
		
		logger.info ( "Updating permessions for appId: " + appId );
		
		try {
			return new PermessionListBean ( 
					daoProvider.get ( ).addPermessionsToApp ( appId, requestItem.getDeveloperId ( ), groupIds ) ); 
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.UnAuthorized, "Missing necessary access to device groups" );
		}
	}
	
	@GET
	@BuildResponse
	@Path ( "/{" + ApiCallerConstants.PathParameters.appId + "}/" + ApiCallerConstants.Path.permessions )
	public PermessionListBean getPermessions ( 
				@PathParam ( ApiCallerConstants.PathParameters.appId )
				String appId ) throws ApiException {
		
		logger.info ( "fetching permessions for appId: " + appId );
		
		return new PermessionListBean ( 
					daoProvider.get ( ).queryItems ( PermessionItem.class, new PermessionItem ( appId ) ) );
	}
}
