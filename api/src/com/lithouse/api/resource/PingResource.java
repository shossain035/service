package com.lithouse.api.resource;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Singleton;
import com.lithouse.api.config.ApiCallerConstants;


@Singleton
@Produces ( MediaType.APPLICATION_JSON )
@Path ( ApiCallerConstants.Path.ping )
public class PingResource {
			
	@GET		
	public String ping ( ) {
		return "pong";
	}

}
