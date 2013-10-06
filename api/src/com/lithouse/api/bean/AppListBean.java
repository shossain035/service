package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.AppItem;


@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class AppListBean extends BaseBean {
	@XmlElement ( name = "apps" )
	private List < AppItem > resultList;
	
	public AppListBean ( List < AppItem > list ) { 
		this.resultList = list;
	}

	public List < AppItem > getList ( ) {
		return resultList;
	}
	public AppListBean ( ) {
	}
}
