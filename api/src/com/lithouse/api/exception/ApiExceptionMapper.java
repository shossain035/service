package com.lithouse.api.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


import com.google.inject.Singleton;
import com.lithouse.api.response.ExceptionBean;

	
@Provider
@Singleton
public class ApiExceptionMapper implements ExceptionMapper < ApiException >
{
	@Override
	public Response toResponse ( final ApiException exception )
	{
		return Response.status ( exception.getErrorCode().getHttpStatus() )
					   .entity ( new ExceptionBean ( exception ) )
					   .type ( MediaType.APPLICATION_JSON )
    		  		   .build();
	}
}

