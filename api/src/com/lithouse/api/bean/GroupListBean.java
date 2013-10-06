package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.GroupItem;


@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class GroupListBean extends BaseBean {
	@XmlElement ( name = "groups" )
	private List < GroupItem > resultList;
	
	public GroupListBean ( List < GroupItem > list ) { 
		this.resultList = list;
	}

	public List < GroupItem > getList ( ) {
		return resultList;
	}
	public GroupListBean ( ) {
	}
}
