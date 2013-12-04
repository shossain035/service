package com.lithouse.trigger;

public class Event {
	private final String deviceId; 
	private final String friendlyName; 
	private final String channel;
	private final String data;
	
	public Event ( String deviceId, String friendlyName, String channel, String data ) {
		this.deviceId = deviceId; 
		this.friendlyName = friendlyName; 
		this.channel = channel;
		this.data = data;
	}

	public String getDeviceId () {
		return deviceId;
	}

	public String getFriendlyName () {
		return friendlyName;
	}

	public String getChannel () {
		return channel;
	}

	public String getData () {
		return data;
	}
}
