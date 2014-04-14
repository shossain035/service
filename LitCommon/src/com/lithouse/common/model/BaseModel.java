package com.lithouse.common.model;

import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;


@XmlSeeAlso ( { DeveloperItem.class, 
				GroupItem.class, 
				DeviceItem.class,
				AppItem.class,
				PermissionItem.class,
				LatestRecordToDeviceItem.class,
				LatestRecordFromDeviceItem.class,
				ContactMessageItem.class,
				SignupItem.class
			} )
public abstract class BaseModel {	
	public BaseModel ( ) {}
	
	protected void verifyNumericField ( Long inputValue, 
										Long min, Long max, 
										String attributeName ) {
		if ( min > inputValue || max < inputValue ) {
			throw new IllegalArgumentException ( 
					"'" + attributeName + "' should be between " + min + " and " + max );
		}
	}
	
	protected void verifyTextFieldLength ( String inputValue, int length, String attributeName ) {
		if ( inputValue != null && inputValue.length ( ) > length )	{	
			throw new IllegalArgumentException ( 
					"'" + attributeName + "' should be shorter than " + length + " characters" );
		}
	}
	
	protected void verifyTextFieldContent ( String inputValue, List < String > checkList, String attributeName ) {
		if ( !checkList.contains ( inputValue ) )	{	
			throw new IllegalArgumentException ( 
					"'" + attributeName + "' is invalid. Valid options: " + checkList.toString ( ) );
		}
	}
	
	protected void verifyTextFieldEmpty ( String inputValue, String attributeName ) {
		if ( inputValue == null || inputValue.isEmpty ( ) ) {
			throw new IllegalArgumentException ( "'" + attributeName + "' can not be null or empty" );
		}
	}
}
