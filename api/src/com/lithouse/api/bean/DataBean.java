package com.lithouse.api.bean;




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
	
	public DataBean ( T result ) {
		this.result = result;
	}
	
	public DataBean ( ) {
	}
}
