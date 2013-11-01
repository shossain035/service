package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.PermissionItem;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class PermissionListBean extends BaseBean {
	//TODO: Remove hard coding from these names 
	@XmlElement ( name = "permissions" )
	private List < PermissionItem > resultList;
	
	public PermissionListBean ( List < PermissionItem > list ) { 
		this.resultList = list;
	}

	public List < PermissionItem > getList ( ) {
		return resultList;
	}
	public PermissionListBean ( ) {
	}
}
