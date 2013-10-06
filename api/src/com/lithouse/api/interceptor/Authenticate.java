package com.lithouse.api.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target ( ElementType.METHOD )
@Retention ( RetentionPolicy.RUNTIME )
public @interface Authenticate {
	public enum Role { DEVELOPER, APP, GROUP };
	
	Role value ( ) default Role.DEVELOPER; 
}
