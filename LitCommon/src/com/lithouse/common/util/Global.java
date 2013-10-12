package com.lithouse.common.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.amazonaws.auth.PropertiesCredentials;

public class Global {
	static private final Logger logger = Logger.getLogger ( "Logger" );
	static private Configuration config = null;
	static private PropertiesCredentials awsCredentials = null;
	static private String awsRegion = null;
	static private String adminId = null;
		
	static {
		try {
			config = new PropertiesConfiguration ( "lithouse.properties" );
			Iterator< String > it = config.getKeys ( );

			while ( it.hasNext ( ) ) {
				String key = it.next ( );
				logger.info ( key + " = " + config.getList ( key ) );
			}
			
			awsCredentials = new PropertiesCredentials (
					Global.class.getResourceAsStream ( "/AwsCredentials.properties" ));
			awsRegion = config.getString ( "aws.region" );
			adminId = config.getString ( "lithouse.admin.id" );
			
		} catch (Exception e) {			
			logger.fatal ( "Global::static:: ", e );
			System.exit( 0 );
		}
	}
	
	static public Logger getLogger ( ) {
		return logger;
	}
	
	static public PropertiesCredentials getAwsCredentials ( ) {
		return awsCredentials;
	}
	
	static public String getAwsRegion ( ) {
		return awsRegion;
	}
	
	static public Configuration getConfig ( ) {
		return config;
	}
	
	static public String getAdminId ( ) {
		return adminId;
	}
	
	public static String generateUniqueName () {		
		return ( "" + Math.random() ).substring ( 2 );
	}
	
	public static String getCurrentTimestamp ( ) {
		DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd-HH.mm.ss.SSS" );
		return dateFormat.format ( new Date ( ) );
	}
}
