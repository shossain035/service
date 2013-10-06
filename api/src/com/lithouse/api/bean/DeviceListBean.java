package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.DeviceItem;


@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class DeviceListBean extends BaseBean {
	@XmlElement ( name = "devices" )
	private List < DeviceItem > resultList;
	
	public DeviceListBean ( List < DeviceItem > list ) { 
		this.resultList = list;
	}

	public List < DeviceItem > getList ( ) {
		return resultList;
	}
	public DeviceListBean ( ) {
	}
}
