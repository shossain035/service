package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.LatestRecord;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class LatestRecordListBean extends BaseBean {
	@XmlElement ( name = "records" )
	private List < LatestRecord > resultList;
	
	public LatestRecordListBean ( List < LatestRecord > list ) { 
		this.resultList = list;
	}

	public List < LatestRecord > getList ( ) {
		return resultList;
	}
	public LatestRecordListBean ( ) {
	}
}
