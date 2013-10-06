package com.lithouse.api.bean;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.PermessionItem;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class PermessionListBean extends BaseBean {
	//TODO: Remove hard coding from these names 
	@XmlElement ( name = "permessions" )
	private List < PermessionItem > resultList;
	
	public PermessionListBean ( List < PermessionItem > list ) { 
		this.resultList = list;
	}

	public List < PermessionItem > getList ( ) {
		return resultList;
	}
	public PermessionListBean ( ) {
	}
}
