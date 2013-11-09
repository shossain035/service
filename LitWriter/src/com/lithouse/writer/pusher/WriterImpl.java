package com.lithouse.writer.pusher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.http.ParseException;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.lithouse.common.model.LatestRecordToDeviceItem;
import com.lithouse.writer.WebSocketData;
import com.lithouse.writer.Writer;
import com.lithouse.writer.WriterExecutor;

public class WriterImpl implements Writer {
	private Logger logger;
	
	@Inject
	public WriterImpl ( Logger logger ) {
		this.logger = logger;
	} 

	@Override
	public void sendRecords ( List < LatestRecordToDeviceItem > records ) throws ParseException, IOException, URISyntaxException {
		Map < EventDataKey, List < String > > eventMap = createEventMap ( records );
		
		for ( EventDataKey eventKey : eventMap.keySet ( ) ) {
			logger.info ( "writing to channel: '" + eventKey.event + "' in " 
							+ eventMap.get ( eventKey ).size ( ) + " devices" );
			
			Pusher.triggerPush ( eventMap.get ( eventKey ), eventKey.event, eventKey.data );
		}
	}
	
	private Map < EventDataKey, List < String > > createEventMap ( List < LatestRecordToDeviceItem > records ) {
		Map < EventDataKey, List < String > > eventMap = new HashMap < EventDataKey, List < String > > ( );
		
		for ( LatestRecordToDeviceItem record : records ) {
			if ( record.getDeviceId ( ) == null ) {
				throw new IllegalArgumentException ( "'deviceId' cannot be null" );
			}
			if ( record.getChannel ( ) == null ) {
				throw new IllegalArgumentException ( "'channel' cannot be null" );
			}
			
			EventDataKey eventKey = new EventDataKey ( record.getChannel ( ), record.getData ( ) );
			List < String > channelNames = eventMap.get ( eventKey );
			if ( channelNames == null ) {
				channelNames = new ArrayList < String > ( );
			}
			
			channelNames.add ( record.getDeviceId ( ) );
			eventMap.put ( eventKey, channelNames );
		}
		
		return eventMap;
	}
	
	private class EventDataKey {
		private final String event;
		private final String data;
		
		public EventDataKey ( String event, String data ) {
			this.event = event;
			this.data = data;
		}
		
		@Override
	    public int hashCode() {
			 return new HashCodeBuilder ( 17, 37 ).
					       append ( event ).
					       append ( data ).
					       toHashCode();
	    }
		 
	    @Override
	    public boolean equals ( Object obj ) {
	        if ( this == obj )
	            return true;
	        if ( obj == null )
	            return false;
	        if ( getClass() != obj.getClass ( ) )
	            return false;
	        final EventDataKey other = ( EventDataKey ) obj;
	        if ( !event.equals ( other.event ) )
	            return false;
	        if ( data == null ) {
	            if ( other.data != null )
	                return false;
	        } else if ( !data.equals ( other.data ) )
	            return false;
	        return true;
	    }
	}

	@Override
	public void updateWebScoketsAsync ( final List < WebSocketData > dataList ) {
		logger.info (  "Sending records to pusher" );
		WriterExecutor.submit ( new Runnable ( ) {
			
			@Override
			public void run () {
				for ( WebSocketData data : dataList ) {
					try {
						Pusher.triggerPush ( 
								data.getId ( ), data.getType ( ).toString ( ), 
								"{\"data\":\"" + data.getData ( ) + "\","
								+ "\"timestamp\":\"" + data.getTimestamp ( ) + "\"," 
								+ "\"channel\":\"" + data.getChannel ( ) + "\"}" );
					} catch ( Exception e ) {
						logger.error ( "Could not connect to pusher.", e );
					}
				}
			}
		} );
	}
}
