package com.lithouse.api.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.bean.LatestRecordFromDeviceListBean;
import com.lithouse.api.bean.LatestRecordToDeviceListBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.Authenticate;
import com.lithouse.api.interceptor.Authenticate.Role;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.RecordDao;
import com.lithouse.common.model.LatestRecordFromDeviceItem;
import com.lithouse.writer.Writer;

@Path ( "/" + ApiCallerConstants.Path.records )
public class DeviceRecordsResource extends BaseResource < RecordDao > {	
	
	@Inject	
	public DeviceRecordsResource ( 
						RequestItem requestItem,
						RequestLogger requestLogger,
						Provider < RecordDao > daoProvider,
						Writer writer ) {
		super ( requestItem, requestLogger, daoProvider );		
	}
	
	@Authenticate ( Role.DEVICE )
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )	
	public DataBean < LatestRecordFromDeviceItem > saveRecordsFromDevice (
								LatestRecordFromDeviceListBean records ) throws ApiException {
				
		if ( records == null || records.getList ( ) == null || records.getList ( ).isEmpty ( ) ) {
			throw new ApiException ( ErrorCode.InvalidInput, "'records' list should contain at least one element" );
		}
		
		logger.info ( records.getList ( ).size ( ) + " records from device: " + requestItem.getDeviceId ( ) );
		try {
			daoProvider.get ( ).saveRecordsFromDevice ( 
					records.getList ( ), 
					requestItem.getGroupId ( ), 
					requestItem.getDeviceId ( ) );
			return new DataBean < LatestRecordFromDeviceItem > ( );
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.UnAuthorized, se.getMessage ( ) );
		}
	}
			
	@Authenticate ( Role.DEVICE )
	@GET
	@BuildResponse
	public LatestRecordToDeviceListBean getRecordsForDevice (  ) throws ApiException {
		
		logger.info ( "records for device: " + requestItem.getDeviceId ( ) );
		
		try {
			return new LatestRecordToDeviceListBean ( 
					daoProvider.get ( ).readLatestRecordsToDevice ( requestItem.getDeviceId ( ) ) );
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.UnAuthorized, se.getMessage ( ) );
		}
	}
		
}
