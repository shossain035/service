package com.lithouse.common.model;

public class Schema {
	public class ApiKey {
		public static final String tableName = "ApiKeysAlpha";
		public static final String developerId = "developerId";
		public static final String apiKey = "apiKey";
	}
	
	public class GroupKey {
		public static final String tableName = "GroupKeysAlpha";
		public static final String developerId = "developerId";
		public static final String groupKey = "groupKey";
		public static final String groupId = "groupId";
	}

	public class AppKey {
		public static final String tableName = "AppKeysAlpha";
		public static final String developerId = "developerId";
		public static final String appKey = "appKey";
		public static final String appId = "appId";
	}
	
	public class DeviceKey {
		public static final String tableName = "DeviceKeysAlpha";
		public static final String deviceKey = "deviceKey";
		public static final String groupId = "groupId";
		public static final String deviceId = "deviceId";		
		public static final String developerId = "developerId";
		public static final String ownerId = "ownerId";
	}
	
	public class Developer {
		public static final String tableName = "DevelopersAlpha";
		public static final String developerId = "developerId";
		public static final String deviceCount = "deviceCount";
		public static final String writeDeviceCount = "writeDeviceCount";
		public static final String appCount = "appCount";
		public static final String firstName = "firstName";
		public static final String lastName = "lastName";
		public static final String apiKey = "apiKey";
		public static final String emailAddress = "emailAddress";
		public static final String deviceLimit = "deviceLimit";
		public static final String IFTTTEmailAddress = "IFTTTEmailAddress";
		public static final String IFTTTActivationStatus = "IFTTTActivationStatus";
	}
	
	public class Group {
		public static final String tableName = "DeviceGroupsAlpha";
		public static final String developerId = "developerId";
		public static final String groupId = "groupId";
		public static final String groupName = "groupName";
		public static final String type = "type";
		public static final String privacy = "privacy";
		public static final String groupKey = "groupKey";
		public static final String description = "description";
	}
	
	public class App {
		public static final String tableName = "AppsAlpha";
		public static final String developerId = "developerId";
		public static final String appId = "appId";
		public static final String appName = "appName";
		public static final String appKey = "appKey";
		public static final String description = "description";
	}
	
	public class Device {
		public static final String tableName = "DevicesAlpha";
		public static final String groupId = "groupId";
		public static final String deviceId = "deviceId";
		public static final String deviceKey = "deviceKey";		
	}
	
	public class Permission {
		public static final String tableName = "PermissionsAlpha";
		public static final String appId = "appId";
		public static final String groupId = "groupId";
		public static final String accessType = "accessType";
	}
	
	public class LatestRecordToDevice {
		public static final String tableName = "LatestWriteToDevicesAlpha";
		public static final String deviceId = "deviceId";
		public static final String timestamp = "timestamp";
		public static final String groupId = "groupId";
		public static final String channel = "channel";
		public static final String data = "data";		
		public static final String appId = "appId";
	}
		
	public class LatestRecordFromDevice {
		public static final String tableName = "LatestWriteFromDevicesAlpha";
		public static final String deviceId = "deviceId";
		public static final String timestamp = "timestamp";
		public static final String groupId = "groupId";
		public static final String channel = "channel";
		public static final String data = "data";
		public static final String rangeKey = "deviceIdChannel";
	}
	
	public class ContactMessage {
		public static final String tableName = "ContactMessagesAlpha";
		public static final String messageId = "messageId";
		public static final String emailAddress = "emailAddress";
		public static final String developerId = "developerId";
		public static final String messageText = "messageText";
	}
}
