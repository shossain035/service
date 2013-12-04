package com.lithouse.api.resource;


import java.util.ArrayList;
import java.util.List;

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
import com.lithouse.trigger.Event;
import com.lithouse.trigger.IFTTTTrigger;
import com.lithouse.trigger.Trigger;
import com.lithouse.writer.WebSocketData;
import com.lithouse.writer.Writer;

@Path ( "/" + ApiCallerConstants.Path.records )
public class DeviceRecordsResource extends BaseResource < RecordDao > {	
	private Writer writer;
	
	@Inject	
	public DeviceRecordsResource ( 
						RequestItem requestItem,
						RequestLogger requestLogger,
						Provider < RecordDao > daoProvider,
						Writer writer ) {
		super ( requestItem, requestLogger, daoProvider );
		this.writer = writer;
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
			
			writer.updateWebScoketsAsync ( prepareSocketData ( records.getList ( ) ) );			
			triggerEvent ( records.getList ( ), requestItem.getGroupId ( ) );
			
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
	
	//TODO: move this to common base
	private List < WebSocketData > prepareSocketData ( 
					List < LatestRecordFromDeviceItem > records ) {
		List < WebSocketData > dataList = new ArrayList < WebSocketData > ( );
		
		for ( LatestRecordFromDeviceItem record : records ) {
			dataList.add ( new WebSocketData ( 
								record.getDeviceId ( ), 
								WebSocketData.Type.LogUpdateWriteFromDevice, 
								record.getChannel ( ), 
								record.getData ( ), 
								record.getTimeStamp ( )) );
		}
		
		return dataList;
	}

	private void triggerEvent ( List < LatestRecordFromDeviceItem > records, String groupId ) {
		//TODO: remove hack
		if ( groupId.equals ( "68f2655e-9718-407a-8226-c955fbd1a284" )) {
			//TODO: Select Trigger based on device/group type		
			Trigger trigger = new IFTTTTrigger ( );
			
			for ( LatestRecordFromDeviceItem record : records) {
				logger.info ( "trigger event for device: " + record.getDeviceId ( )  );
				
				trigger.triggerEventAsync ( 
						new Event ( record.getDeviceId ( ), 
									"Lithouse device", 
									record.getChannel ( ), 
									record.getData ( ) ) );
			}
		}		
	}
}
