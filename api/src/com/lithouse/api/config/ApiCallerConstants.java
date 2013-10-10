package com.lithouse.api.config;

public class ApiCallerConstants {
	public class QueryParameters {
		public static final String apiKey = "apiKey";
		public static final String appKey = "appKey";
		public static final String groupKey = "groupKey";
		public static final String count = "count";
		public static final String groupId = "groupId";
		public static final String deviceId = "deviceId";
		public static final String channel = "channel";
	}
	
	public class Path {
		public static final String root = "/v1/*";
		public static final String developers = "/developers";
		public static final String devices = "devices";
		public static final String groups = "/groups";
		public static final String apps = "/apps";
		public static final String ping = "/ping";
		public static final String permessions = "permessions";
		public static final String records = "records";
		public static final String record = "record";
	}
	
	public class PathParameters {
		public static final String developerId = "developerId";
		public static final String deviceId = "deviceId";
		public static final String appId = "appId";
		public static final String groupId = "groupId";
	}
}
