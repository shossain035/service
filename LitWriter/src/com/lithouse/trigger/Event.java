package com.lithouse.trigger;

public class Event {
	private final String deviceId; 
	private final String friendlyName; 
	private final String channel;
	private final String data;
	private final String ownerIFTTTEmailAddress;
	
	public Event ( String deviceId, String friendlyName, String channel, String data, String ownerIFTTTEmailAddress ) {
		this.deviceId = deviceId; 
		this.friendlyName = friendlyName; 
		this.channel = channel;
		this.data = data;
		this.ownerIFTTTEmailAddress = ownerIFTTTEmailAddress;
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
	
	public String getOwnerIFTTTEmailAddress ( ) {
		return ownerIFTTTEmailAddress;
	}
}
