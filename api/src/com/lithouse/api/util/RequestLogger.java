package com.lithouse.api.util;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.lithouse.common.util.Global;

@Singleton
public class RequestLogger extends Logger {	
	private final Logger logger = Logger.getLogger( "RequestLogger" );
	private final Provider < RequestItem > requestItemProvider;
	
	@Inject
	public RequestLogger ( Provider < RequestItem > requestItemProvider ) {
		super ( "RequestLogger" );
		this.requestItemProvider = requestItemProvider;
		Global.getLogger ( ).info ( "creating RequestLogger");
	}
	
	
	public void logRequestComplete () {
		RequestItem currentRequest = requestItemProvider.get();
		logger.info ( currentRequest.getRequestName() 
				+ "time: " 
				+ ( System.currentTimeMillis() - currentRequest.getStartTime() ) 
				+ " ms" );		
	}
		
	@Override
	public void info ( Object message ) {		
		RequestItem currentRequest = requestItemProvider.get();
		logger.info ( currentRequest.getRequestName() + message );
	}
	
	@Override
	public void info ( Object message,  Throwable t ) {
		RequestItem currentRequest = requestItemProvider.get();
		logger.info ( currentRequest.getRequestName() + message, t );
	}
	
	@Override
	public void debug ( Object message ) {
		RequestItem currentRequest = requestItemProvider.get();
		logger.debug ( currentRequest.getRequestName() + message );
	}
	
	@Override
	public void error ( Object message,  Throwable t ) {
		RequestItem currentRequest = requestItemProvider.get();
		logger.error ( currentRequest.getRequestName() + message, t );
	}
	
	@Override
	public void error ( Object message ) {
		RequestItem currentRequest = requestItemProvider.get();
		logger.error ( currentRequest.getRequestName() + message );
	}

	
//	@Inject
//	public RequestLogger ( Provider < RequestItem > requestItemProvider ) {
//		this.requestItemProvider = requestItemProvider;  
//	}
//	
//	public void logRequestComplete () {
//		RequestItem currentRequest = requestItemProvider.get();
//		logger.info( currentRequest.getRequestName() 
//				+ "time: " 
//				+ ( System.currentTimeMillis() - currentRequest.getStartTime() ) 
//				+ " ms" );		
//	}
//		
//	public void info ( String message ) {		
//		RequestItem currentRequest = requestItemProvider.get();
//		logger.info( currentRequest.getRequestName() + message );
//	}
//	
//	public void info ( String message,  Throwable t ) {
//		RequestItem currentRequest = requestItemProvider.get();
//		logger.info( currentRequest.getRequestName() + message, t );
//	}
//	
//	public void debug ( String message ) {
//		RequestItem currentRequest = requestItemProvider.get();
//		logger.debug( currentRequest.getRequestName() + message );
//	}
//	
//	public void error ( String message,  Throwable t ) {
//		RequestItem currentRequest = requestItemProvider.get();
//		logger.error( currentRequest.getRequestName() + message, t );
//	}
//	
//	public void error ( String message ) {
//		RequestItem currentRequest = requestItemProvider.get();
//		logger.error( currentRequest.getRequestName() + message );
//	}
	
}
