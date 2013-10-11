package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.LatestRecordFromDeviceItem;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class LatestRecordFromDeviceListBean extends BaseBean {
	@XmlElement ( name = "records" )
	private List < LatestRecordFromDeviceItem > resultList;
	
	public LatestRecordFromDeviceListBean ( List < LatestRecordFromDeviceItem > list ) { 
		this.resultList = list;
	}

	public List < LatestRecordFromDeviceItem > getList ( ) {
		return resultList;
	}
	public LatestRecordFromDeviceListBean ( ) {
	}
}
