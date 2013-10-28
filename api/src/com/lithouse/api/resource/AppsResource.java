package com.lithouse.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.AppListBean;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.Authenticate;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.AppDao;
import com.lithouse.common.model.AppItem;


@Path ( ApiCallerConstants.Path.apps )
public class AppsResource extends BaseResource < AppDao > {
			
	private Provider < AppResource > appProvider; 
	
	@Inject	
	public AppsResource ( RequestItem requestItem,
				    	  RequestLogger requestLogger,
				    	  Provider < AppDao > daoProvider,
						  Provider < AppResource > appProvider ) {
		super ( requestItem, requestLogger, daoProvider );
		this.appProvider = appProvider;		
	}
	
	@Authenticate
	@GET 
	@BuildResponse
	public AppListBean getAllAppsByDeveloperId ( ) {
		logger.info ( "fetching apps by developerId: " + requestItem.getDeveloperId ( ) );
		return new AppListBean ( 
						daoProvider.get ( ).getAllApps ( requestItem.getDeveloperId ( ) ) );
	}  
	
	@Authenticate
	@Path ( "/" )	
	public AppResource getAppResource ( ) throws ApiException {		
	    return appProvider.get ( );
	}
	
	@Authenticate
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < AppItem > createApp ( AppItem appItem ) throws ApiException {				
		appItem.setDeveloperId ( requestItem.getDeveloperId ( ) );
		logger.info ( "creating app " + appItem.getAppName ( ) 
				+ " for developerId: " + requestItem.getDeveloperId ( ) );
		
		try { 
			return new DataBean < AppItem > ( daoProvider.get ( ).createApp ( appItem ) );
		} catch ( IllegalArgumentException e ) {
			throw new ApiException ( ErrorCode.InvalidInput, e. getMessage ( ) );
		}
	}
	
}
