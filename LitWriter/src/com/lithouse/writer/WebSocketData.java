package com.lithouse.writer;

public class WebSocketData {
	private final String id;
	private final Type type;
	private final String channel;
	private final String data;
	private final String timestamp;	
	
	public WebSocketData ( 
			String id, Type type, String channel, String data, String timestamp ) {
		this.id = id;
		this.type = type;
		this.channel = channel;
		this.data = data;
		this.timestamp = timestamp;
	}
	
	public String getId () {
		return id;
	}
	public Type getType () {
		return type;
	}
	public String getData () {
		return data;
	}
		
	public String getChannel () {
		return channel;
	}

	public String getTimestamp () {
		return timestamp;
	}

	public enum Type {
		LogUpdateWriteToDevice ( "writeToDevice" ),
		LogUpdateWriteFromDevice ( "writeFromDevice" );
	 
		private String typeCode;
	 
		private Type ( String s ) {
			typeCode = s;
		}
	 
		@Override
		public String toString () {
			return typeCode;
		}
	 
	}
}
