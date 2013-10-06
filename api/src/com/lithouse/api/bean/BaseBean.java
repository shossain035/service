package com.lithouse.api.bean;





public class BaseBean {
	
	protected String requestId;
	
	public BaseBean () {
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId ( String requestId ) {
		this.requestId = requestId;
	} 
}
