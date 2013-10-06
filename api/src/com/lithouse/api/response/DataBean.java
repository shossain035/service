package com.lithouse.api.response;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.lithouse.common.model.BaseModel;

@XmlRootElement
@XmlAccessorType ( XmlAccessType.FIELD )
public class DataBean < T extends BaseModel > extends BaseBean {
	@XmlElementRef
	private T result;
	@XmlElementRef
	private List < T > resultList;
	
	public DataBean ( List < T > list ) { 
		this.resultList = list;
	}

	public DataBean ( T result ) {
		this.result = result;
	}
	
	public DataBean ( ) {
	}
}
