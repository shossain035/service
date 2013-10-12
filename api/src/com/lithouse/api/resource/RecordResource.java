package com.lithouse.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.bean.DataBean;
import com.lithouse.api.bean.LatestRecordFromDeviceListBean;
import com.lithouse.api.bean.LatestRecordToDeviceListBean;
import com.lithouse.api.config.ApiCallerConstants;
import com.lithouse.api.exception.ApiException;
import com.lithouse.api.exception.ApiException.ErrorCode;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.RecordDao;
import com.lithouse.common.model.LatestRecordFromDeviceItem;
import com.lithouse.writer.Writer;


public class RecordResource extends BaseResource < RecordDao > {	
	
	@Inject	
	public RecordResource ( RequestItem requestItem,
				    	    RequestLogger requestLogger,
				    	    Provider < RecordDao > daoProvider,
				    	    Writer writer ) {
		super ( requestItem, requestLogger, daoProvider );		
	}
		
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )	
	public DataBean < LatestRecordFromDeviceItem > writeFromDevice (
								@PathParam ( ApiCallerConstants.PathParameters.groupId ) 
								String groupId,
								@PathParam ( ApiCallerConstants.PathParameters.deviceId ) 
								String deviceId,
								LatestRecordFromDeviceListBean records ) throws ApiException {
		
		verifyGroupId ( groupId );
		verifyRecords ( records.getList ( ), groupId, deviceId );
		
		logger.info ( records.getList ( ).size ( ) + " records from device: " + deviceId );
		try {
			daoProvider.get ( ).saveRecordsFromDevice ( records.getList ( ), groupId, deviceId );
			return new DataBean < LatestRecordFromDeviceItem > ( );
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.UnAuthorized, se.getMessage ( ) );
		}
	}
		
	private void verifyRecords ( List < LatestRecordFromDeviceItem > records, String groupId, String deviceId ) throws ApiException {

		if ( records == null || records.isEmpty ( ) ) {
			throw new ApiException ( ErrorCode.InvalidInput, "'records' list should contain at least one element" );
		}

		
		for ( LatestRecordFromDeviceItem record : records ) {
			if ( record.getChannel ( ) == null || record.getChannel ( ).isEmpty ( ) ) {
				throw new ApiException ( ErrorCode.InvalidInput, Arrays.asList ( "channel" )  );
			}
			
			record.setGroupId ( groupId );
			record.setDeviceId ( deviceId );
		}
	}
	
	@GET
	@BuildResponse
	public LatestRecordToDeviceListBean getRecordsForDevice ( 
				@PathParam ( ApiCallerConstants.PathParameters.groupId ) 
				String groupId,
				@PathParam ( ApiCallerConstants.PathParameters.deviceId ) 
				String deviceId ) throws ApiException {
		
		verifyGroupId ( groupId );
		
		logger.info ( "records for device: " + deviceId );
		
		try {
			return new LatestRecordToDeviceListBean ( 
					daoProvider.get ( ).readLatestRecordsToDevice ( groupId, deviceId ) );
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.UnAuthorized, se.getMessage ( ) );
		}
	}
	
	private void verifyGroupId ( String groupId ) throws ApiException {
		if ( groupId == null || !groupId.equalsIgnoreCase ( requestItem.getGroupId ( ) ) ) {
			throw new ApiException ( ErrorCode.UnAuthorized, 
					Arrays.asList ( ApiCallerConstants.QueryParameters.groupKey )  );
		}
	}
}