package com.lithouse.api.resource;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.bean.LitDeviceListBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.model.LitDeviceItem;
import com.lithouse.common.util.Global;


@Path ( ApiCallerConstants.Path.devicesLit )
public class LitDevicesResource extends BaseResource < GenericDao > {
			
	private final Provider < ReviewsResource > reviewProvider;
	
	@Inject	
	public LitDevicesResource ( 
			RequestItem requestItem,
			RequestLogger requestLogger,
			Provider < GenericDao > daoProvider,
			Provider < ReviewsResource > reviewProvider ) {
		super ( requestItem, requestLogger, daoProvider );
		this.reviewProvider = reviewProvider;
	}
	
//	//@Authenticate
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < LitDeviceItem > saveScannedDevices (
			LitDeviceListBean devicesBean, 
			@QueryParam ( ApiCallerConstants.QueryParameters.scannerId ) 
			String scannerId ) throws ApiException {
		
		List < LitDeviceItem > devices = devicesBean.getList ( );
		
		if ( devices == null || devices.isEmpty ( )) 
			return new DataBean < LitDeviceItem > ( );
		
		logger.info ( "saving " + devices.size ( ) 
						+ " devices of scannerId: " + scannerId );
		String date = Global.getCurrentTimestamp ( );
		
		for ( LitDeviceItem device : devices ) {
			device.setScannerId ( scannerId );
			device.setScannedDate ( date );
		}
		
		daoProvider.get ( ).batchSave ( devices );
		
		return new DataBean < LitDeviceItem > ( );
	}
	
	@Path ( "/{" + ApiCallerConstants.PathParameters.deviceType + "}" 
			+ "/" + ApiCallerConstants.Path.reviews )	
	public ReviewsResource getDeviceReviewResource ( ) {
		return reviewProvider.get ( );
	}
}
