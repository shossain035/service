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
import com.lithouse.common.dao.DeveloperDao;
import com.lithouse.common.dao.RecordDao;
import com.lithouse.common.model.DeveloperItem;
import com.lithouse.common.model.LatestRecordFromDeviceItem;
import com.lithouse.trigger.Event;
import com.lithouse.trigger.IFTTTTrigger;
import com.lithouse.trigger.Trigger;
import com.lithouse.writer.WebSocketData;
import com.lithouse.writer.Writer;

@Path ( "/" + ApiCallerConstants.Path.records )
public class DeviceRecordsResource extends BaseResource < RecordDao > {	
	private Writer writer;
	private Provider < DeveloperDao > developerDaoProvider;
	
	@Inject	
	public DeviceRecordsResource ( 
						RequestItem requestItem,
						RequestLogger requestLogger,
						Provider < RecordDao > daoProvider,
						Provider < DeveloperDao > developerDaoProvider,
						Writer writer ) {
		super ( requestItem, requestLogger, daoProvider );
		this.writer = writer;
		this.developerDaoProvider = developerDaoProvider;
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
		
		logger.info ( records.getList ( ).size ( ) + " records from device: " + requestItem.getDeviceKey ( ).getDeviceId ( ) );
		try {
			daoProvider.get ( ).saveRecordsFromDevice ( 
					records.getList ( ), 
					requestItem.getDeviceKey ( ).getGroupId ( ), 
					requestItem.getDeviceKey ( ).getDeviceId ( ) );
			
			writer.updateWebScoketsAsync ( prepareSocketData ( records.getList ( ) ) );			
			triggerEvent ( records.getList ( ) );
			
			return new DataBean < LatestRecordFromDeviceItem > ( );
		} catch ( SecurityException se ) {
			throw new ApiException ( ErrorCode.UnAuthorized, se.getMessage ( ) );
		}
	}
			
	@Authenticate ( Role.DEVICE )
	@GET
	@BuildResponse
	public LatestRecordToDeviceListBean getRecordsForDevice (  ) throws ApiException {
		
		logger.info ( "records for device: " + requestItem.getDeviceKey ( ).getDeviceId ( ) );
		
		try {
			return new LatestRecordToDeviceListBean ( 
					daoProvider.get ( ).readLatestRecordsToDevice ( requestItem.getDeviceKey ( ).getDeviceId ( ) ) );
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

	private void triggerEvent ( List < LatestRecordFromDeviceItem > records ) {		
		if ( requestItem.getDeviceKey ( ).getOwnerId ( ) == null ) return;
		
		DeveloperItem owner = developerDaoProvider.get ( )
				.findWithIFTTTVerification ( requestItem.getDeviceKey ( ).getOwnerId ( ) );
		
		
		if ( owner == null || owner.getIFTTTEmailAddress ( ) == null 
				|| !DeveloperItem.ActivationStatus.SUCCESS.equalsIgnoreCase ( owner.getIFTTTActivationStatus ( ) ) ) return; 
		
		//TODO: Select Trigger based on device/group type. Could be null trigger		
		Trigger trigger = new IFTTTTrigger ( );
		
		for ( LatestRecordFromDeviceItem record : records) {
			logger.info ( "trigger event for device: " + record.getDeviceId ( )  );
			
			trigger.triggerEventAsync ( 
					new Event ( record.getDeviceId ( ), 
								"Lithouse device", 
								record.getChannel ( ), 
								record.getData ( ),
								owner.getIFTTTEmailAddress ( ) ) );
		}
						
	}
}
