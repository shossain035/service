package com.lithouse.api.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.google.inject.Singleton;
import com.lithouse.api.bean.AppListBean;
import com.lithouse.api.bean.DeviceListBean;
import com.lithouse.api.bean.GroupListBean;
import com.lithouse.api.bean.LatestRecordListBean;
import com.lithouse.api.bean.PermessionListBean;
import com.lithouse.api.bean.RecordToDeviceListBean;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Singleton
@Provider
public class JAXBContextResolver implements ContextResolver < JAXBContext > {

	private JAXBContext context;
	private Class < ? > [ ] types = { AppListBean.class, 
									  DeviceListBean.class,
									  GroupListBean.class, 
									  PermessionListBean.class, 
									  RecordToDeviceListBean.class,
									  LatestRecordListBean.class
									};
	
	public JAXBContextResolver ( ) throws Exception {
		this.context = new JSONJAXBContext ( JSONConfiguration.natural ( ).build ( ), types );
	}
	
	public JAXBContext getContext ( Class < ? > objectType ) {     
		for ( Class < ? > type : types ) {
            if ( type == objectType ) {
                return context;
            }
        }
		
		return null;
	}
}