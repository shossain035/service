package com.lithouse.api.exception;

import java.util.List;

import javax.ws.rs.core.Response.Status;


public class ApiException extends Exception {

	private static final long serialVersionUID = 3695746557869146327L;
	
	private ErrorCode code;
	private String message;
	private String requestId;
	
	public enum ErrorCode {		
		InternalError ( "InternalError", Status.INTERNAL_SERVER_ERROR ),
		UnAuthenticated ( "AuthenticationError", Status.UNAUTHORIZED ),
		UnAuthorized ( "AuthorizationError", Status.UNAUTHORIZED ),
		InvalidInput ( "InvalidInput", Status.BAD_REQUEST );
						
		private String appStatus;
		private Status httpStatus;
		
		ErrorCode ( String appStatus, Status httpStatus ) {
			this.appStatus = appStatus;
			this.httpStatus = httpStatus;
		}
		
		public String getAppStatus () {
			return appStatus;
		}
		
		public Status getHttpStatus () {
			return httpStatus;
		}
	} 
	
	public ApiException () {
		
	}
	
	public ApiException ( ErrorCode code ) {
		this.code = code;		
	}
	
	public ApiException ( ErrorCode code, String param, String message ) {
		this.code = code;
		this.message = "'" + param + "' " + message;
	}
	
	public ApiException ( ErrorCode code, String message ) {
		this.code = code;
		this.message = message;
	}
	
	public ApiException ( ErrorCode code, 
						  List < String > parameterList ) {
		String message = "Provide valid '" + parameterList.get ( 0 ) + "'";

		for ( int i = 1; i < parameterList.size ( ); i++  ) {
			message += ", '" + parameterList.get ( i ) + "'";
		}
		
		this.code = code;
		this.message = message;
	}
	
	public ErrorCode getErrorCode () {
		return code;
	}
	
	public String getMessage () {
		return message;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
