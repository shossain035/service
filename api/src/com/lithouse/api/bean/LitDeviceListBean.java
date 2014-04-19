package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.LitDeviceItem;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class LitDeviceListBean extends BaseBean {
	@XmlElement ( name = "devices" )
	private List < LitDeviceItem > resultList;
	
	public LitDeviceListBean ( List < LitDeviceItem > list ) { 
		this.resultList = list;
	}

	public List < LitDeviceItem > getList ( ) {
		return resultList;
	}
	public LitDeviceListBean ( ) {
	}
}
