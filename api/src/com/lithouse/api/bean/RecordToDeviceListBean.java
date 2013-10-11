package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.LatestRecordToDeviceItem;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class RecordToDeviceListBean extends BaseBean {
	@XmlElement ( name = "records" )
	private List < LatestRecordToDeviceItem > resultList;
	
	public RecordToDeviceListBean ( List < LatestRecordToDeviceItem > list ) { 
		this.resultList = list;
	}

	public List < LatestRecordToDeviceItem > getList ( ) {
		return resultList;
	}
	public RecordToDeviceListBean ( ) {
	}
}
