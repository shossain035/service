package com.lithouse.api.response;





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
