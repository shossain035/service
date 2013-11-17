package com.lithouse.api.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.lithouse.api.provider.JAXBContextResolver;
import com.lithouse.api.resource.BaseResource;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletScopes;
import com.lithouse.api.exception.ApiExceptionMapper;
import com.lithouse.api.interceptor.Authenticate;
import com.lithouse.api.interceptor.AuthenticationInterceptor;
import com.lithouse.api.interceptor.BuildResponse;
import com.lithouse.api.interceptor.BuildResponseInterceptor;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;
import com.lithouse.common.dao.AppDao;
import com.lithouse.common.dao.DeviceDao;
import com.lithouse.common.dao.GroupDao;
import com.lithouse.common.dao.GenericDao;
import com.lithouse.common.dao.RecordDao;
import com.lithouse.common.dao.impl.AppDaoImpl;
import com.lithouse.common.dao.impl.DeviceDaoImpl;
import com.lithouse.common.dao.impl.GroupDaoImpl;
import com.lithouse.common.dao.impl.GenericDaoImpl;
import com.lithouse.common.dao.impl.RecordDaoImpl;
import com.lithouse.common.util.Global;
import com.lithouse.smtp.EmailSender;
import com.lithouse.smtp.EmailSenderImpl;
import com.lithouse.writer.Writer;
import com.lithouse.writer.WriterExecutor;
import com.lithouse.writer.pusher.WriterImpl;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ApiConfig extends GuiceServletContextListener {

	@Override
    protected Injector getInjector() {
        return Guice.createInjector ( new JerseyServletModule() {

            @Override
            protected void configureServlets() {
            	
            	bind ( Logger.class )
					.to ( RequestLogger.class );
            	bind ( GenericDao.class )
        			.to ( GenericDaoImpl.class )
        			.in ( ServletScopes.REQUEST );
            	bind ( GroupDao.class )
    				.to ( GroupDaoImpl.class )
    				.in ( ServletScopes.REQUEST );
            	bind ( DeviceDao.class )
					.to ( DeviceDaoImpl.class )
					.in ( ServletScopes.REQUEST );
            	bind ( AppDao.class )
    				.to ( AppDaoImpl.class )
    				.in ( ServletScopes.REQUEST );
            	bind ( RecordDao.class )
					.to ( RecordDaoImpl.class )
					.in ( ServletScopes.REQUEST );
            	
            	bind ( Writer.class )
					.to ( WriterImpl.class )
					.in ( ServletScopes.REQUEST );            	
            	
            	bind ( EmailSender.class )
            		.to ( EmailSenderImpl.class )
            		.in ( ServletScopes.REQUEST );
            	
            	bind (ApiExceptionMapper.class );
                
            	bindInterceptor ( Matchers.any(), Matchers.annotatedWith ( Authenticate.class ), 
			  	        		  new AuthenticationInterceptor ( 
				  						  getProvider ( RequestItem.class ), 
				  						  getProvider ( RequestLogger.class ),
				  						  getProvider ( HttpServletRequest.class ),
				  						  getProvider ( GenericDao.class ) ) );
  	
            	bindInterceptor ( Matchers.any(), Matchers.annotatedWith ( BuildResponse.class ), 
            	        		  new BuildResponseInterceptor ( 
	        	        				  getProvider ( RequestItem.class ), 
				  						  getProvider ( RequestLogger.class ) ) );            	
            	
            	Map < String, String > params = new HashMap < String, String >();
                params.put ( PackagesResourceConfig.PROPERTY_PACKAGES,
                			 BaseResource.class.getPackage ( ).getName ( )
                			 + "," + JAXBContextResolver.class.getPackage ( ).getName ( ) );
                
                serve ( ApiCallerConstants.Path.root ).with ( GuiceContainer.class, params );            
            }
        });
	}
	
	public void contextDestroyed ( ServletContextEvent servletContextEvent ) {
		Global.getLogger ( ).info ( "Cleaning up..." );
		WriterExecutor.shutdown ( );
	}
}
