package com.lithouse.api.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
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
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.interceptor.Authenticate.Role;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.RecordDao;
import com.lithouse.common.model.LatestRecordToDeviceItem;
import com.lithouse.common.util.Global;


public class RecordsResource extends BaseResource < RecordDao > {
		
	@Inject	
	public RecordsResource ( RequestItem requestItem,
					    	 RequestLogger requestLogger,
					    	 Provider < RecordDao > daoProvider ) {
		super ( requestItem, requestLogger, daoProvider );
	}
	
	@Authenticate ( Role.APP )
	@GET
	@BuildResponse
	public LatestRecordFromDeviceListBean getLatestRecords ( 
								@PathParam ( ApiCallerConstants.PathParameters.groupId ) 
								String groupId,
								@QueryParam ( ApiCallerConstants.QueryParameters.deviceId )
								List < String > deviceIds,
								@QueryParam ( ApiCallerConstants.QueryParameters.channel )
								List < String > channelNames ) throws ApiException {
		
		logger.info ( "reading devices from group: " 
				+ groupId + " for developerId: " + requestItem.getDeveloperId ( ) );
		
		try {
			return new LatestRecordFromDeviceListBean ( 
					daoProvider.get ( ).readLatestRecordsFromDevices ( 
									deviceIds, channelNames, requestItem.getAppId ( ), groupId, requestItem.getDeveloperId ( ) ) );			
		} catch ( SecurityException e ) {
			throw new ApiException ( ErrorCode.UnAuthorized, e.getMessage ( ) );
		}
	}
	
	@Authenticate ( Role.APP )
	@POST
	@BuildResponse
	@Consumes ( MediaType.APPLICATION_JSON )
	public DataBean < LatestRecordToDeviceItem > writeToDevices ( 
								@PathParam ( ApiCallerConstants.PathParameters.groupId ) 
								String groupId,
								LatestRecordToDeviceListBean recordsToDevices ) throws ApiException {
		
		//TODD: Allow broadcast to group
		verifyRecordsForWriting ( recordsToDevices.getList ( ), requestItem.getAppId ( ), groupId, false );
		
		logger.info ( "writing to devices of group: " + groupId + " by app: " + requestItem.getAppId ( ) );
		try {
			daoProvider.get ( ).saveRecordsToDevices ( recordsToDevices.getList ( ), 
					requestItem.getAppId ( ), groupId, requestItem.getDeveloperId ( ) );
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.UnAuthorized, se.getMessage ( ) );
		}
		
//		//TODO: make the call asynchronus
//		try {
//			writer.sendRecords ( recordsToDevices.getList ( ) );
//		} catch ( Exception e ) {
//			throw new ApiException ( ErrorCode.InternalError, "Could not send records to devices" );
//		}
		return new DataBean < LatestRecordToDeviceItem > ( );
	}
	
	private void verifyRecordsForWriting ( 
			List < LatestRecordToDeviceItem > records, String appId, String groupId, boolean isBroadcast ) 
						throws ApiException {
		
		if ( records == null || records.isEmpty ( )) {
			throw new ApiException ( ErrorCode.InvalidInput, "'records' list should contain at least one element" );
		}
				
		String timestamp = Global.getCurrentTimestamp ( );
		
		for ( LatestRecordToDeviceItem record : records ) {
			if ( null == record.getChannel ( ) || record.getChannel ( ).isEmpty ( ) ) {
				throw new ApiException ( ErrorCode.InvalidInput, "'channel' cannot be blank" );
			} 
			//first record
			if ( !isBroadcast && record.getDeviceId ( ) == null ) {
				throw new ApiException ( ErrorCode.InvalidInput, "at least one record is missing 'deviceId'" );
			}
			
			record.setAppId ( appId );
			record.setGroupId ( groupId );
			record.setTimeStamp ( timestamp );
		}
	}
}
