package com.lithouse.api.interceptor;







import org.aopalliance.intercept.MethodInvocation;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lithouse.api.response.BaseBean;
import com.lithouse.api.util.RequestItem;
import com.lithouse.api.util.RequestLogger;

public class BuildResponseInterceptor extends BaseInterceptor {
	
	@Inject
	public BuildResponseInterceptor ( Provider < RequestItem > requestItemProvider,
							 		  Provider < RequestLogger > loggerProvider ) {
		super ( requestItemProvider, loggerProvider);				
	}

	@Override	
	public Object invoke ( MethodInvocation invocation, 
						   String methodName,
						   RequestItem requestItem, 
						   RequestLogger logger ) throws Throwable {

		try {
			logger.info ( "@BuildResponse::" + methodName );			
			
			BaseBean methodReturn = ( BaseBean ) invocation.proceed();
			
			methodReturn.setRequestId ( requestItem.getRequestId() );			
			return methodReturn;			
		} finally {
			logger.logRequestComplete ();			
		}
	 }
}
