package com.lithouse.writer.pusher;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.activity.InvalidActivityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.inject.Singleton;

@Singleton
public class Pusher {
	static private final Logger logger = Logger.getLogger( "Pusher" );
	/**
	 *  Pusher Host name
	 */
	private final static String pusherHost = "api.pusherapp.com";
	
	/**
	 * Pusher Application Identifier
	 */
	private final static String pusherApplicationId = "52257";
	
	/**
	 * Pusher Application Key
	 */
	private final static String pusherApplicationKey = "b6111c1f50a8befbe267";
	
	/**
	 * Pusher Secret
	 */
	private final static String pusherApplicationSecret = "03fef23cd90d52b39e2d";
	
	private static String executeHttpMethod ( HttpRequestBase httpMethod ) throws ParseException, IOException {
		httpMethod.addHeader ( "Content-Type", "application/json" );
		DefaultHttpClient httpclient = new DefaultHttpClient ( );
		HttpResponse response = httpclient.execute ( httpMethod );
		
		if ( response.getStatusLine ( ).getStatusCode ( ) != HttpStatus.SC_OK 
				&& response.getStatusLine ( ).getStatusCode ( ) != HttpStatus.SC_ACCEPTED ) {
			logger.error ( "HTTP error: " + response.getStatusLine ( ).getStatusCode ( ) ); 
			logger.error ( "reason: " + EntityUtils.toString ( response.getEntity ( ) ));
			return null;
		}
	    			    		    
        return EntityUtils.toString ( response.getEntity ( ) );	        			
	}
		
	/**
	 * Converts a byte array to a string representation
	 * @param data
	 * @return
	 */
	private static String byteArrayToString(byte[] data){
    	BigInteger bigInteger = new BigInteger(1,data);
    	String hash = bigInteger.toString(16);
    	// Zero pad it
    	while(hash.length() < 32 ){
    	  hash = "0" + hash;
    	}
    	return hash;
	}
	
	/**
	 * Returns a md5 representation of the given string
	 * @param data
	 * @return
	 */
	private static String md5Representation(String data) {
        try {
        	//Get MD5 MessageDigest
        	MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        	byte[] digest = messageDigest.digest(data.getBytes("US-ASCII"));
        	return byteArrayToString(digest);
        } catch (NoSuchAlgorithmException nsae) {
            //We should never come here, because GAE has a MD5 algorithm
        	throw new RuntimeException("No MD5 algorithm");
        } catch (UnsupportedEncodingException e) {
            //We should never come here, because UTF-8 should be available
        	throw new RuntimeException("No UTF-8");
		}	
	}
	
	/**
	 * Returns a HMAC/SHA256 representation of the given string
	 * @param data
	 * @return
	 */
    private static String hmacsha256Representation(String data) {
        try {
            // Create the HMAC/SHA256 key from application secret
            final SecretKeySpec signingKey = new SecretKeySpec( pusherApplicationSecret.getBytes(), "HmacSHA256");

            // Create the message authentication code (MAC)
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            
            //Process and return data
            byte[] digest = mac.doFinal(data.getBytes("UTF-8"));
            digest = mac.doFinal(data.getBytes());
            //Convert to string
            BigInteger bigInteger = new BigInteger(1,digest);
    		return String.format("%0" + (digest.length << 1) + "x", bigInteger);            
        } catch (NoSuchAlgorithmException nsae) {
            //We should never come here, because GAE has HMac SHA256
        	throw new RuntimeException("No HMac SHA256 algorithm");
        } catch (UnsupportedEncodingException e) {
            //We should never come here, because UTF-8 should be available
        	throw new RuntimeException("No UTF-8");
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Invalid key exception while converting to HMac SHA256");
		} 
    }
    
    /**
     * Build query string that will be appended to the URI and HMAC/SHA256 encoded
     * @param eventName
     * @param jsonData
     * @return
     */
    private static String buildQuery(String jsonBody){
    	StringBuffer buffer = new StringBuffer();
    	//Auth_Key
    	buffer.append("auth_key=");
    	buffer.append(pusherApplicationKey);
    	//Timestamp
    	buffer.append("&auth_timestamp=");
    	buffer.append(System.currentTimeMillis() / 1000);
    	//Auth_version
    	buffer.append("&auth_version=1.0");
    	//MD5 body
    	buffer.append("&body_md5=");
    	buffer.append(md5Representation(jsonBody));
    	//Return content of buffer
		return buffer.toString();
    }
    

    /**
     * Build query string that will be appended to the URI and HMAC/SHA256 encoded
     * @param eventName
     * @param jsonData
     * @return
     */
    private static String buildQuery(String eventName, String jsonData, String socketID){
    	StringBuffer buffer = new StringBuffer();
    	//Auth_Key
    	buffer.append("auth_key=");
    	buffer.append(pusherApplicationKey);
    	//Timestamp
    	buffer.append("&auth_timestamp=");
    	buffer.append(System.currentTimeMillis() / 1000);
    	//Auth_version
    	buffer.append("&auth_version=1.0");
    	//MD5 body
    	buffer.append("&body_md5=");
    	buffer.append(md5Representation(jsonData));
    	//Event Name
    	buffer.append("&name=");
    	buffer.append(eventName);
		//Append socket id if set
		if (!socketID.isEmpty()) {
			buffer.append("&socket_id=");
			buffer.append(socketID);
		}
		//Return content of buffer
		return buffer.toString();
    }
    
    /**
     * Build path of the URI that is also required for Authentication
     * @return
     */
    private static String buildURIPath(){
    	StringBuffer buffer = new StringBuffer();
    	//Application ID
    	buffer.append("/apps/");
    	buffer.append(pusherApplicationId);
    	//Event
    	buffer.append("/events");    	
		//Return content of buffer
		return buffer.toString();    	
    }
    
    /**
     * Build path of the URI that is also required for Authentication
     * @return
     */
    private static String buildURIPath(String channelName){
    	StringBuffer buffer = new StringBuffer();
    	//Application ID
    	buffer.append("/apps/");
    	buffer.append(pusherApplicationId);
    	//Channel name
    	buffer.append("/channels/");
    	buffer.append(channelName);
    	//Event
    	buffer.append("/events");    	
		//Return content of buffer
		return buffer.toString();    	
    }
    
    /**
     * Build authentication signature to assure that our event is recognized by Pusher
     * @param uriPath
     * @param query
     * @return
     */
    private static String buildAuthenticationSignature(String uriPath, String query){
    	StringBuffer buffer = new StringBuffer();
    	//request method
    	buffer.append("POST\n");
    	//URI Path
    	buffer.append(uriPath);
    	buffer.append("\n");
    	//Query string
    	buffer.append(query);
    	//Encode data
    	String h = buffer.toString();
    	return hmacsha256Representation(h);    	
    }
    
    /**
     * Build URI where request is send to
     * @param uriPath
     * @param query
     * @param signature
     * @return
     */
    private static String buildURIString(String uriPath, String query, String signature){
    	StringBuffer buffer = new StringBuffer();
    	//Protocol
    	buffer.append("http://");
    	//Host
    	buffer.append(pusherHost);
    	//URI Path
    	buffer.append(uriPath);
    	//Query string
    	buffer.append("?");
    	buffer.append(query);
    	//Authentication signature
    	buffer.append("&auth_signature=");
    	buffer.append(signature);

    	return buffer.toString ( );
    }
    
    /**
     * Delivers a message to the Pusher API without providing a socket_id
     * @param channel
     * @param event
     * @param jsonData
     * @return
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws ParseException 
     */
    public static void triggerPush(String channel, String event, String jsonData) throws ParseException, IOException, URISyntaxException{
    	triggerPush(channel, event, jsonData, "");
    }
    
    /**
     * Delivers a message to the Pusher API
     * @param channel
     * @param event
     * @param jsonData
     * @param socketId
     * @return
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws ParseException 
     */
    public static void triggerPush(String channel, String event, String jsonData, String socketId) throws ParseException, IOException, URISyntaxException{
    	//Build URI path
    	String uriPath = buildURIPath(channel);
    	//Build query
    	String query = buildQuery(event, jsonData, socketId);
    	//Generate signature
    	String signature = buildAuthenticationSignature(uriPath, query);
    	
    	HttpPost httpPost = new HttpPost ( buildURIString (uriPath, query, signature ) );
    	httpPost.setEntity ( new ByteArrayEntity ( jsonData.getBytes ( ) ) );
		String responseJson = executeHttpMethod ( httpPost );
		
		if ( responseJson == null ) {
			throw new InvalidActivityException ( "Could not push event" );
		}
		
		logger.info ( responseJson );
    	    	
    }

    /**
     * Delivers messages to the Pusher API
     * @param channelNames
     * @param event
     * @param jsonData
     * @param socketId
     * @return
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws ParseException 
     */
    public static void triggerPush(List  <String> channelNames, String event, String data ) throws ParseException, IOException, URISyntaxException{
    	//Build URI path
    	String uriPath = buildURIPath ( );
    	String jsonBody = buildBody ( channelNames, event, data );
    	//Build query
    	String query = buildQuery (  jsonBody );
    	//Generate signature
    	String signature = buildAuthenticationSignature(uriPath, query);
    	
    	HttpPost httpPost = new HttpPost ( buildURIString (uriPath, query, signature ) );
    	httpPost.setEntity ( new ByteArrayEntity ( jsonBody.getBytes ( ) ) );
		String responseJson = executeHttpMethod ( httpPost );
		
		if ( responseJson == null ) {
			throw new InvalidActivityException ( "Could not push event" );
		}
		
		logger.info ( responseJson );
    	    	
    }

    private static String buildBody (List <String> channelNames, String event, String data ) {
    	StringBuilder sb = new StringBuilder ( "{\"name\":\"" + event + "\",\"channels\":[\"" + channelNames.get ( 0 ) + "\"" );
    	
    	for ( int i =1 ; i< channelNames.size ( ) ; i++ ) {
    		sb.append ( ",\"" );
    		sb.append ( channelNames.get ( i ) );
    		sb.append ( "\"" );	
    	}
    	
    	sb.append ( "],\"data\":\"" );
    	sb.append ( data );
    	sb.append ( "\"}" );
    	return sb.toString ( );
    }

}
