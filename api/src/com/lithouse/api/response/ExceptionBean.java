package com.lithouse.api.response;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.api.exception.ApiException;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class ExceptionBean extends BaseBean {
	private String errorCode;
	private String message;	

	public ExceptionBean ( ) { }

	public ExceptionBean ( ApiException exception ) {
		errorCode = exception.getErrorCode().getAppStatus ( );
		message = exception.getMessage ( );
		setRequestId ( exception.getRequestId ( ) );
	}

	public String getErrorCode ( ) {
		return errorCode;
	}

	public String getMessage ( ) {
		return message;
	}
}
