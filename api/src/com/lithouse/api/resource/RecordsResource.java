package com.lithouse.api.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.bean.RecordToDeviceListBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.RecordDao;
import com.lithouse.common.model.RecordToDevice;
import com.lithouse.writer.Writer;


public class RecordsResource extends BaseResource < RecordDao > {
	private Writer writer;
	
	@Inject	
	public RecordsResource ( RequestItem requestItem,
					    	 RequestLogger requestLogger,
					    	 Provider < RecordDao > daoProvider,
					    	 Writer writer ) {
		super ( requestItem, requestLogger, daoProvider );
		this.writer = writer;
	}
	
	
//	@GET
//	@BuildResponse
//	public DataBean < WriteFromDevice > getLatestRecords ( 
//								@PathParam ( ApiCallerConstants.PathParameters.groupId ) 
//								String groupId,
//								@QueryParam ( ApiCallerConstants.QueryParameters.de ) ) throws ApiException {
//		
//		if ( null == daoProvider.get ( ).find ( GroupItem.class, requestItem.getDeveloperId ( ), groupId ) ) {
//			throw new ApiException ( ErrorCode.UnAuthenticated, 
//									 "You do not have access to the devices in this group" );
//		}
//		
//		logger.info ( "fetching devices from group: " 
//				+ groupId + " for developerId: " + requestItem.getDeveloperId ( ) );
//		
//		try {
//			return new DataBean < DeviceItem > ( 
//	    				daoProvider.get ( ).getAllDevices ( groupId ) );
//		} catch ( IllegalArgumentException e ) {
//			throw new ApiException ( ErrorCode.InvalidInput, e.getMessage ( ) );
//		}
//	}
//	
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < RecordToDevice > writeToDevice ( 
								@PathParam ( ApiCallerConstants.PathParameters.groupId ) 
								String groupId,
								RecordToDeviceListBean recordsToDevices ) throws ApiException {
		
		//TODD: Allow broadcast to group
		verifyRecords ( recordsToDevices.getList ( ), false );
		
		logger.info ( "writing to devices of group: " + groupId + " by app: " + requestItem.getAppId ( ) );
		try {
			daoProvider.get ( ).saveRecordsToDevices ( recordsToDevices.getList ( ), 
					requestItem.getAppId ( ), groupId, requestItem.getDeveloperId ( ) );
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.UnAuthorized, se.getMessage ( ) );
		}
		
		//TODO: make the call asynchronus
		try {
			writer.sendRecords ( recordsToDevices.getList ( ) );
		} catch ( Exception e ) {
			throw new ApiException ( ErrorCode.InternalError, "Could not send records to devices" );
		}
		return new DataBean < RecordToDevice > ( );
	}
	
	private void verifyRecords ( List < RecordToDevice > records, boolean isBroadcast ) 
						throws ApiException {
		
		if ( records == null || records.isEmpty ( )) {
			throw new ApiException ( ErrorCode.InvalidInput, "'records' list should contain at least one element" );
		}
				
		for ( RecordToDevice record : records ) {
			if ( null == record.getChannel ( ) || record.getChannel ( ).isEmpty ( ) ) {
				throw new ApiException ( ErrorCode.InvalidInput, "'channel' cannot be blank" );
			} 
			//first record
			if ( !isBroadcast && record.getDeviceId ( ) == null ) {
				throw new ApiException ( ErrorCode.InvalidInput, "at least one record is missing 'deviceId'" );
			}
		}
	}
}
